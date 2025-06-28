package com.tpdesi.repositorys;

import com.tpdesi.entitys.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {
    List<Familia> findByActivaTrue();
    List<Familia> findByActivaTrueAndNroFamiliaContainingIgnoreCase(String nroFamilia);
    List<Familia> findByActivaTrueAndNombreContainingIgnoreCase(String nombre); 
    Optional<Familia> findByNroFamilia(String nroFamilia);
}