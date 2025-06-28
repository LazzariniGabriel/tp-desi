package com.tpdesi.repositorys;

import com.tpdesi.entitys.Preparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {

//    este metodo sera usado para traer las preparaciones cuyo estado booleano recetaActiva sea true.
    List<Preparacion> findByRecetaActivaTrue();


//    metodo para filtrar por fechas y por nombre de la receta
List<Preparacion> findByFechaPreparacion(LocalDate fecha);

    @Query("SELECT p FROM Preparacion p JOIN p.seleccionDeRecetas r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Preparacion> findByNombreDeReceta(@Param("nombre") String nombre);


}
