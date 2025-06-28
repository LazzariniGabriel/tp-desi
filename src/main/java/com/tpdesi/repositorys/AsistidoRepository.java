package com.tpdesi.repositorys;

import com.tpdesi.entitys.Asistido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsistidoRepository extends JpaRepository<Asistido, Long> {
    Optional<Asistido> findByDni(Long dni); 
}