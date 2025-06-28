package com.tpdesi.repositorys;

import com.tpdesi.entitys.Preparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Añadido para métodos que devuelven un solo resultado

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {

    // CAMBIO: Ahora busca preparaciones activas (campo 'activa')
    List<Preparacion> findByActivaTrue();

    // CAMBIO: Método para encontrar si ya existe una preparación de una receta en una fecha específica
    Optional<Preparacion> findByReceta_IdAndFechaPreparacion(Long recetaId, LocalDate fecha);

    // Método para filtrar preparaciones por fecha
    List<Preparacion> findByFechaPreparacionAndActivaTrue(LocalDate fecha); // Asegura que solo lista las activas

    // CAMBIO: Query para filtrar por nombre de la receta asociada a la preparación
    @Query("SELECT p FROM Preparacion p WHERE p.activa = true AND LOWER(p.receta.nombre) LIKE LOWER(CONCAT('%', :nombreReceta, '%'))")
    List<Preparacion> findByRecetaNombreContainingIgnoreCaseAndActivaTrue(@Param("nombreReceta") String nombreReceta);

    // Buscar preparaciones activas por fecha Y nombre de receta (si ambos filtros están presentes)
    @Query("SELECT p FROM Preparacion p WHERE p.activa = true AND p.fechaPreparacion = :fecha AND LOWER(p.receta.nombre) LIKE LOWER(CONCAT('%', :nombreReceta, '%'))")
    List<Preparacion> findByFechaPreparacionAndRecetaNombreContainingIgnoreCaseAndActivaTrue(@Param("fecha") LocalDate fecha, @Param("nombreReceta") String nombreReceta);
}