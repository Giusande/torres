package com.industria.torres;
import java.util.*;
import org.springframework.data.jpa.repository.*;
public final class Repositorios {
  private Repositorios(){}
  public interface SinalizacaoRepository extends JpaRepository<Sinalizacao,Long> {
    @EntityGraph(attributePaths="atendimentos")
    @Query("select distinct s from Sinalizacao s order by s.registradoEm desc")
    List<Sinalizacao> findAllComAtendimentos();
  }
  public interface EstadoAlvoRepository extends JpaRepository<EstadoAlvo,Long> {
    Optional<EstadoAlvo> findByTorreNumeroAndAlvo(int torreNumero,String alvo);
  }
}
