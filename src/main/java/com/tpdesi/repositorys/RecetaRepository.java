package com.tpdesi.repositorys;

import com.tpdesi.entitys.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    Optional<Receta> findByNombreIgnoreCase(String nombre);
    List<Receta> findByActivaTrue(); // Listar todas las recetas activas
    List<Receta> findByActivaTrueAndNombreContainingIgnoreCase(String nombre); // Filtrar por nombre

    // Para filtrar por rango de calorías (se necesitará lógica adicional en el servicio)
    // Spring Data JPA puede hacer esto si 'caloriasTotales' fuera una columna
    // Pero como es un @Transient, el filtro debe ser en el servicio después de obtener las recetas.
}