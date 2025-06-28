package com.tpdesi.repositorys;

import com.tpdesi.entitys.EntregaAsistencia;
import com.tpdesi.entitys.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaAsistenciaRepository extends JpaRepository<EntregaAsistencia, Long> { 
    Optional<EntregaAsistencia> findByFamiliaAndFecha(Familia familia, LocalDate fecha); 

    List<EntregaAsistencia> findByFechaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreContainingIgnoreCase( 
            LocalDate fecha, String nroFamilia, String nombreFamilia);
    
    List<EntregaAsistencia> findByActivoTrue(); 
}