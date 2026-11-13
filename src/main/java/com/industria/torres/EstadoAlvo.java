package com.industria.torres;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="estado_alvo")
public class EstadoAlvo {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(name="torre_numero") public int torreNumero;
  public String alvo, condicao;
  @Column(name="atualizado_em") public Instant atualizadoEm;
}
