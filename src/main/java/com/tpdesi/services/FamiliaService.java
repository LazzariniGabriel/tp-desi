package com.tpdesi.services;

import com.tpdesi.entitys.Familia;
import com.tpdesi.repositorys.FamiliaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FamiliaService {

    private final FamiliaRepository familiaRepository;

    public FamiliaService(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    public Familia crearFamilia(Familia familia) {
        familia.setFechaAlta(LocalDate.now());
        return familiaRepository.save(familia);
    }

    public List<Familia> listarActivas() {
        return familiaRepository.findByActivaTrue();
    }

    public Familia modificarFamilia(Familia familiaActualizada) {
        return familiaRepository.save(familiaActualizada);
    }

    public void eliminarLogicamente(Long id) {
        Familia familia = familiaRepository.findById(id).orElseThrow();
        familia.setActiva(false);
        familiaRepository.save(familia);
    }

    public Familia obtenerPorId(Long id) {
        return familiaRepository.findById(id).orElseThrow();
    }
}
