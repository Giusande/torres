
package com.industria.torres.repository;

import com.industria.torres.Sinalizacao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface SinalizacaoRepository
        extends JpaRepository<Sinalizacao, Long> {

    @EntityGraph(attributePaths = "atendimentos")
    @Query("select distinct s from Sinalizacao s order by s.registradoEm desc")
    List<Sinalizacao> findAllComAtendimentos();
}
