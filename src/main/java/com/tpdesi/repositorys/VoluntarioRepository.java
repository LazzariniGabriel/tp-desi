package com.tpdesi.repositorys;

import com.tpdesi.model.Voluntario; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
   
}