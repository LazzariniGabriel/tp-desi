package com.tpdesi.repositorys;

import com.tpdesi.entitys.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamiliaRepository extends JpaRepository<Familia, Long> {
    List<Familia> findByActivaTrue();
}
