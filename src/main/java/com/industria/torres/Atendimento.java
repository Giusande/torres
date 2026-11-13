package com.industria.torres;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="atendimento")
public class Atendimento {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="sinalizacao_id") public Sinalizacao sinalizacao;
  public String situacao, responsavel, descricao;
  public Instant em;
}
