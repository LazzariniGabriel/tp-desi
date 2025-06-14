package com.tpdesi.repositorys;

// Importamos la entidad Entrega y Familia, que serán utilizadas para construir consultas
import com.tpdesi.entitys.Entrega;
import com.tpdesi.entitys.Familia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Esta interfaz se encarga de acceder a la base de datos para la entidad Entrega
@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    // Hereda de JpaRepository, lo que nos da métodos listos para usar: save, findById, deleteById, etc.
    // <Entrega, Long>: indica que esta interfaz trabaja con la entidad Entrega y su ID es de tipo Long

    /**
     * Busca si ya existe una entrega para una familia en una fecha determinada
     * Se usa para evitar entregar dos veces el mismo día a la misma familia
     */
    Optional<Entrega> findByFamiliaAndFechaEntrega(Familia familia, LocalDate fecha);

    /**
     * Obtiene todas las entregas registradas en un día específico
     * Se utiliza para listar entregas por fecha
     */
    List<Entrega> findByFechaEntrega(LocalDate fecha);

    /**
     * Busca entregas por fecha y permite además filtrar por número y nombre de familia
     * Los métodos "ContainingIgnoreCase" permiten buscar aunque no se escriba todo el texto exacto ni en mayúsculas
     */
    List<Entrega> findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
            LocalDate fecha, String nroFamilia, String nombreFamilia);
}
