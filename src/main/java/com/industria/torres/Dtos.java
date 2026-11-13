package com.industria.torres;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.List;
public final class Dtos {
  private Dtos(){}
  public record Item(@NotBlank @Size(max=60) String componente, @NotBlank String justificativa, @Min(0) int fotos){}
  public record NovaSinalizacao(@Min(1) int torre, @NotNull @Pattern(regexp="G|[1-9][0-9]?") String alvo,
      @NotNull @Pattern(regexp="[DPI]") String condicao, @NotNull @Valid List<Item> itens){}
  public record NovoAtendimento(@NotNull @Pattern(regexp="[AER]") String situacao, @Size(max=120) String responsavel, String descricao){}
  public record AtendimentoView(String situacao,String responsavel,String descricao,Instant em){}
  public record SinalizacaoView(Long id,int torre,String alvo,String componente,String justificativa,int fotos,
      String condicao,String situacao,Instant registradoEm,List<AtendimentoView> atendimentos){}
  public record AlvoView(int torre,String alvo,String condicao,Instant atualizadoEm){}
  public record Estado(List<AlvoView> alvos,List<SinalizacaoView> sinalizacoes){}
}
