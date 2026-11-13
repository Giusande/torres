
package com.industria.torres;

import static org.springframework.http.HttpStatus.*;

import com.industria.torres.Dtos.*;
import com.industria.torres.repository.SinalizacaoRepository;
import com.industria.torres.repository.EstadoAlvoRepository;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SinalizacaoService {

    private final SinalizacaoRepository sinRepo;
    private final EstadoAlvoRepository estRepo;

    @Value("${app.conjuntos:6}")
    private int conjuntos;

    public SinalizacaoService(
            SinalizacaoRepository s,
            EstadoAlvoRepository e
    ) {
        this.sinRepo = s;
        this.estRepo = e;
    }

    @Transactional(readOnly = true)
    public Estado estado() {

        var sin = sinRepo.findAllComAtendimentos()
                .stream()
                .map(s -> new SinalizacaoView(
                        s.id,
                        s.torreNumero,
                        s.alvo,
                        s.componente,
                        s.justificativa,
                        s.fotos,
                        s.condicao,
                        s.situacao,
                        s.registradoEm,
                        s.atendimentos.stream()
                                .map(a -> new AtendimentoView(
                                        a.situacao,
                                        a.responsavel,
                                        a.descricao,
                                        a.em
                                ))
                                .toList()
                ))
                .toList();

        var alvos = estRepo.findAll()
                .stream()
                .map(e -> new AlvoView(
                        e.torreNumero,
                        e.alvo,
                        e.condicao,
                        e.atualizadoEm
                ))
                .toList();

        return new Estado(alvos, sin);
    }

    /**
     * Registra uma sinalização sem sobrescrever os registros anteriores
     * e atualiza a condição do conjunto ou área.
     */
    @Transactional
    public void registrar(NovaSinalizacao n) {

        if (!"G".equals(n.alvo())
                && Integer.parseInt(n.alvo()) > conjuntos) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Conjunto inexistente"
            );
        }

        if (n.itens().isEmpty() && !"D".equals(n.condicao())) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Selecione ao menos um componente"
            );
        }

        Instant agora = Instant.now();

        if (n.itens().isEmpty()) {

            sinRepo.save(
                    nova(
                            n,
                            null,
                            "Sem pendências registradas",
                            0,
                            "-",
                            agora
                    )
            );
        }

        n.itens().forEach(i ->
                sinRepo.save(
                        nova(
                                n,
                                i.componente(),
                                i.justificativa().trim(),
                                i.fotos(),
                                "A",
                                agora
                        )
                )
        );

        var est = estRepo
                .findByTorreNumeroAndAlvo(n.torre(), n.alvo())
                .orElseGet(EstadoAlvo::new);

        est.torreNumero = n.torre();
        est.alvo = n.alvo();
        est.condicao = n.condicao();
        est.atualizadoEm = agora;

        estRepo.save(est);
    }

    private Sinalizacao nova(
            NovaSinalizacao n,
            String comp,
            String just,
            int fotos,
            String sit,
            Instant ts
    ) {

        var s = new Sinalizacao();

        s.torreNumero = n.torre();
        s.alvo = n.alvo();
        s.componente = comp;
        s.justificativa = just;
        s.fotos = fotos;
        s.condicao = n.condicao();
        s.situacao = sit;
        s.registradoEm = ts;

        return s;
    }

    /**
     * Atualiza a situação da pendência preservando o registro original
     * e o histórico de atendimentos.
     */
    @Transactional
    public void atender(Long id, NovoAtendimento a) {

        var s = sinRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                NOT_FOUND,
                                "Pendência não encontrada"
                        )
                );

        if (s.componente == null) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Registro sem pendência não pode ser atualizado"
            );
        }

        if ("R".equals(a.situacao())
                && (a.descricao() == null || a.descricao().isBlank())) {

            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Descreva a solução aplicada"
            );
        }

        var at = new Atendimento();

        at.sinalizacao = s;
        at.situacao = a.situacao();
        at.responsavel = a.responsavel();
        at.descricao = a.descricao() == null
                ? null
                : a.descricao().trim();
        at.em = Instant.now();

        s.atendimentos.add(at);
        s.situacao = a.situacao();
    }
}
