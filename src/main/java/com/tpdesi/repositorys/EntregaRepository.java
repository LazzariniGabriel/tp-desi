package com.tpdesi.repositorys;

import com.tpdesi.entitys.Entrega;
import com.tpdesi.entitys.Familia;
import com.tpdesi.entitys.Preparacion; // Importar Preparacion
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    // CAMBIO: findByFamiliaAndFechaEntrega ahora usa la relación con Preparacion si fuera necesario
    // Si la regla de negocio es: NO DOS ENTREGAS A LA MISMA FAMILIA EN EL MISMO DÍA (independiente de la preparación)
    Optional<Entrega> findByFamiliaAndFechaEntrega(Familia familia, LocalDate fecha);

    // Listado de entregas por fecha y con filtros de familia
    List<Entrega> findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
            LocalDate fecha, String nroFamilia, String nombreFamilia);

    // Método para buscar entregas activas (si tu lógica de listado solo quiere las activas)
    List<Entrega> findByActivoTrue(); 
}