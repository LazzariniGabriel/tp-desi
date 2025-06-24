package com.tpdesi.repositorys;

import com.tpdesi.entitys.Preparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {


}
