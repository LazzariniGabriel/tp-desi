package com.tpdesi.repositorys;

import com.tpdesi.entitys.IngredienteReceta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredienteRecetaRepository extends JpaRepository<IngredienteReceta, Long> {
    
}