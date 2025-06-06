package com.tpdesi.repositorys;

import com.tpdesi.entitys.Integrante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntegranteRepository extends JpaRepository<Integrante, Long> {
    Optional<Integrante> findByDni(Long dni);
}