package com.industria.torres;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
@Entity @Table(name="sinalizacao")
public class Sinalizacao {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(name="torre_numero") public int torreNumero;
  public String alvo, componente, justificativa, condicao, situacao;
  public int fotos;
  @Column(name="registrado_em") public Instant registradoEm;
  @OneToMany(mappedBy="sinalizacao",cascade=CascadeType.ALL) @OrderBy("em asc")
  public List<Atendimento> atendimentos=new ArrayList<>();
}
