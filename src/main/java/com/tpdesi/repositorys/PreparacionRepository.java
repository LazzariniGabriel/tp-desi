package com.tpdesi.repositorys;

import com.tpdesi.entitys.Preparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {

    List<Preparacion> findByActivaTrue();

    Optional<Preparacion> findByReceta_IdAndFechaCoccion(Long recetaId, LocalDate fechaCoccion); 

    List<Preparacion> findByFechaCoccionAndActivaTrue(LocalDate fechaCoccion);

    @Query("SELECT p FROM Preparacion p WHERE p.activa = true AND LOWER(p.receta.nombre) LIKE LOWER(CONCAT('%', :nombreReceta, '%'))")
    List<Preparacion> findByRecetaNombreContainingIgnoreCaseAndActivaTrue(@Param("nombreReceta") String nombreReceta);

    @Query("SELECT p FROM Preparacion p WHERE p.activa = true AND p.fechaCoccion = :fechaCoccion AND LOWER(p.receta.nombre) LIKE LOWER(CONCAT('%', :nombreReceta, '%'))")
    List<Preparacion> findByFechaCoccionAndRecetaNombreContainingIgnoreCaseAndActivaTrue(@Param("fechaCoccion") LocalDate fechaCoccion, @Param("nombreReceta") String nombreReceta);
}