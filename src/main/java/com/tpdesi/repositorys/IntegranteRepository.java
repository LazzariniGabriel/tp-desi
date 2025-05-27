package com.tpdesi.repositorys;

import com.tpdesi.entitys.Integrante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegranteRepository extends JpaRepository<Integrante, Long> {
    boolean existsByDni(Long dni);
}
