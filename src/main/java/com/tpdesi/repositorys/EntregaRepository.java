package com.tpdesi.repositorys;

import com.tpdesi.entitys.Entrega;
import com.tpdesi.entitys.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    Optional<Entrega> findByFamiliaAndFechaEntrega(Familia familia, LocalDate fecha);
    List<Entrega> findByFechaEntrega(LocalDate fecha);
    List<Entrega> findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
            LocalDate fecha, String nroFamilia, String nombreFamilia);
}
