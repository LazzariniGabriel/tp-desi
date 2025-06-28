package com.tpdesi.repositorys;

import com.tpdesi.entitys.ItemReceta; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRecetaRepository extends JpaRepository<ItemReceta, Long> { 
}