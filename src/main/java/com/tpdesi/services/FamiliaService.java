package com.tpdesi.services;

import com.tpdesi.entitys.Familia;
import com.tpdesi.entitys.Asistido; 
import com.tpdesi.repositorys.FamiliaRepository;
import com.tpdesi.repositorys.PersonaRepository;
import com.tpdesi.repositorys.AsistidoRepository; 
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FamiliaService {

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private AsistidoRepository asistidoRepository; 

    @Autowired
    private PersonaRepository personaRepository; 

    @Transactional
    public Familia altaFamilia(Familia familia) {
        for (Asistido asistido : familia.getIntegrantes()) { 
            // DNI es único a nivel de Persona
            if (asistido.getId() == null && personaRepository.findByDni(asistido.getDni()).isPresent()) { 
                throw new IllegalArgumentException("Ya existe una persona con DNI: " + asistido.getDni());
            }
            asistido.setFamilia(familia);
            asistido.setFechaRegistro(LocalDate.now()); 
        }

        familia.setNroFamilia(generarNumeroFamiliaAsistida());
        familia.setFechaRegistro(LocalDate.now()); 
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

        familiaExistente.setNombre(familiaActualizada.getNombre());
        familiaExistente.setFechaUltimaAsistenciaRecibida(familiaActualizada.getFechaUltimaAsistenciaRecibida());

        List<Asistido> integrantesActuales = familiaExistente.getIntegrantes(); 
        List<Asistido> integrantesNuevos = familiaActualizada.getIntegrantes(); 

        // Identificar integrantes a eliminar lógicamente
        integrantesActuales.forEach(integranteExistente -> {
            boolean found = integrantesNuevos.stream()
                    .anyMatch(integranteNuevo -> integranteNuevo.getId() != null && integranteNuevo.getId().equals(integranteExistente.getId()));
            if (!found) {
                integranteExistente.setActivo(false);
            }
        });

        // Actualizar o agregar integrantes
        for (Asistido integranteNuevo : integrantesNuevos) { 
            if (integranteNuevo.getId() == null) {
                // DNI es único a nivel de Persona
                if (personaRepository.findByDni(integranteNuevo.getDni()).isPresent()) { // Busca DNI en Persona
                    throw new IllegalArgumentException("Ya existe una persona con DNI: " + integranteNuevo.getDni());
                }
                familiaExistente.addIntegrante(integranteNuevo);
                integranteNuevo.setFechaRegistro(LocalDate.now()); 
            } else {
                integrantesActuales.stream()
                        .filter(i -> i.getId().equals(integranteNuevo.getId()))
                        .findFirst()
                        .ifPresent(integranteExistente -> {
                            // Si el DNI cambia, verificar unicidad a nivel de Persona
                            if (!integranteExistente.getDni().equals(integranteNuevo.getDni())) {
                                if (personaRepository.findByDni(integranteNuevo.getDni())
                                        .filter(p -> !p.getId().equals(integranteNuevo.getId())) // Asegura que no sea el mismo ID
                                        .isPresent()) {
                                    throw new IllegalArgumentException("Ya existe una persona con DNI: " + integranteNuevo.getDni());
                                }
                            }
                            // Actualizar todos los campos de Persona y Asistido
                            integranteExistente.setDni(integranteNuevo.getDni());
                            integranteExistente.setApellido(integranteNuevo.getApellido());
                            integranteExistente.setNombre(integranteNuevo.getNombre());
                            integranteExistente.setFechaNacimiento(integranteNuevo.getFechaNacimiento());
                            integranteExistente.setDomicilio(integranteNuevo.getDomicilio()); 
                            integranteExistente.setOcupacion(integranteNuevo.getOcupacion());
                            integranteExistente.setActivo(integranteNuevo.isActivo());
                            // fechaRegistro no se modifica, solo en alta
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
            return familiaRepository.findByActivaTrueAndNombreContainingIgnoreCase(filtro);
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