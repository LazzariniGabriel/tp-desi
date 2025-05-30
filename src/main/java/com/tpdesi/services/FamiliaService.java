package com.tpdesi.services;

import com.tpdesi.entitys.Familia;
import com.tpdesi.entitys.Integrante;
import com.tpdesi.repositorys.FamiliaRepository;
import com.tpdesi.repositorys.IntegranteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FamiliaService {

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private IntegranteRepository integranteRepository;

    @Transactional
    public Familia altaFamilia(Familia familia) {
        for (Integrante integrante : familia.getIntegrantes()) {
            if (integrante.getId() == null && integranteRepository.findByDni(integrante.getDni()).isPresent()) {
                throw new IllegalArgumentException("Ya existe un integrante con DNI: " + integrante.getDni());
            }
            integrante.setFamilia(familia);
        }

        familia.setNroFamilia(generarNumeroFamiliaAsistida());
        familia.setFechaAlta(LocalDate.now());
        familia.setActiva(true);

        return familiaRepository.save(familia);
    }

    @Transactional
    public Familia bajaFamilia(Long id) {
        Familia familia = familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con ID: " + id));
        familia.setActiva(false);
        return familiaRepository.save(familia);
    }

    @Transactional
    public Familia modificarFamilia(Long id, Familia familiaActualizada) {
        Familia familiaExistente = familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con ID: " + id));

        familiaExistente.setNombreFamilia(familiaActualizada.getNombreFamilia());
        familiaExistente.setFechaUltimaAsistenciaRecibida(familiaActualizada.getFechaUltimaAsistenciaRecibida());

        List<Integrante> integrantesActuales = familiaExistente.getIntegrantes();
        List<Integrante> integrantesNuevos = familiaActualizada.getIntegrantes();

        // Identificar integrantes a eliminar lógicamente
        integrantesActuales.forEach(integranteExistente -> {
            boolean found = integrantesNuevos.stream()
                    .anyMatch(integranteNuevo -> integranteNuevo.getId() != null && integranteNuevo.getId().equals(integranteExistente.getId()));
            if (!found) {
                integranteExistente.setActivo(false);
            }
        });

        // Actualizar o agregar integrantes
        for (Integrante integranteNuevo : integrantesNuevos) {
            if (integranteNuevo.getId() == null) {
                if (integranteRepository.findByDni(integranteNuevo.getDni()).isPresent()) {
                    throw new IllegalArgumentException("Ya existe un integrante con DNI: " + integranteNuevo.getDni());
                }
                familiaExistente.addIntegrante(integranteNuevo);
            } else {
                integrantesActuales.stream()
                        .filter(i -> i.getId().equals(integranteNuevo.getId()))
                        .findFirst()
                        .ifPresent(integranteExistente -> {
                            if (!integranteExistente.getDni().equals(integranteNuevo.getDni())) {
                                if (integranteRepository.findByDni(integranteNuevo.getDni())
                                        .filter(i -> !i.getId().equals(integranteNuevo.getId()))
                                        .isPresent()) {
                                    throw new IllegalArgumentException("Ya existe un integrante con DNI: " + integranteNuevo.getDni());
                                }
                            }
                            integranteExistente.setDni(integranteNuevo.getDni());
                            integranteExistente.setApellido(integranteNuevo.getApellido());
                            integranteExistente.setNombre(integranteNuevo.getNombre());
                            integranteExistente.setFechaNacimiento(integranteNuevo.getFechaNacimiento());
                            integranteExistente.setOcupacion(integranteNuevo.getOcupacion());
                            integranteExistente.setActivo(integranteNuevo.isActivo());
                        });
            }
        }
        return familiaRepository.save(familiaExistente);
    }

    public List<Familia> listarFamiliasActivas() {
        return familiaRepository.findByActivaTrue();
    }

    public List<Familia> buscarFamilias(String filtro, String tipoFiltro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return familiaRepository.findByActivaTrue();
        } else if ("nroFamilia".equalsIgnoreCase(tipoFiltro)) {
            return familiaRepository.findByActivaTrueAndNroFamiliaContainingIgnoreCase(filtro);
        } else if ("nombre".equalsIgnoreCase(tipoFiltro)) {
            return familiaRepository.findByActivaTrueAndNombreFamiliaContainingIgnoreCase(filtro);
        } else {
            return familiaRepository.findByActivaTrue();
        }
    }

    public Familia obtenerFamiliaPorId(Long id) {
        return familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con ID: " + id));
    }

    private String generarNumeroFamiliaAsistida() {
        return "FAM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}