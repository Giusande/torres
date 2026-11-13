
package com.industria.torres.repository;

import com.industria.torres.EstadoAlvo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoAlvoRepository
        extends JpaRepository<EstadoAlvo, Long> {

    Optional<EstadoAlvo> findByTorreNumeroAndAlvo(
        int torreNumero,
        String alvo
    );
}
