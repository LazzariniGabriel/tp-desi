package com.tpdesi.services;

import com.tpdesi.entitys.Asistido;
import com.tpdesi.model.Persona;
import com.tpdesi.model.Voluntario;
import com.tpdesi.repositorys.AsistidoRepository;
import com.tpdesi.repositorys.PersonaRepository;
import com.tpdesi.repositorys.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private AsistidoRepository asistidoRepository;
    @Autowired
    private VoluntarioRepository voluntarioRepository; 

    @Override
    @Transactional
    public Persona crearPersona(Persona persona) {
        if (personaRepository.findByDni(persona.getDni()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una persona con el DNI: " + persona.getDni());
        }
        return personaRepository.save(persona);
    }

    @Override
    public Optional<Persona> buscarPersonaPorDni(Long dni) {
        return personaRepository.findByDni(dni);
    }

    @Override
    public List<Persona> listarTodasLasPersonas() {
        return personaRepository.findAll();
    }

    @Override
    @Transactional
    public Asistido crearAsistido(Asistido asistido) {
        if (personaRepository.findByDni(asistido.getDni()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un asistido con el DNI: " + asistido.getDni());
        }
        return asistidoRepository.save(asistido);
    }

    @Override
    @Transactional
    public Voluntario crearVoluntario(Voluntario voluntario) {
        if (personaRepository.findByDni(voluntario.getDni()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un voluntario con el DNI: " + voluntario.getDni());
        }
        return voluntarioRepository.save(voluntario);
    }

    @Override
    public Optional<Asistido> findAsistidoById(Long id) {
        return asistidoRepository.findById(id);
    }

    @Override
    public Optional<Voluntario> findVoluntarioById(Long id) {
        return voluntarioRepository.findById(id);
    }
}