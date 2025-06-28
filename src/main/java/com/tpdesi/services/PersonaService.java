package com.tpdesi.services;

import com.tpdesi.model.Persona;
import com.tpdesi.model.Voluntario;
import com.tpdesi.entitys.Asistido;

import java.util.List;
import java.util.Optional;

public interface PersonaService {
    Persona crearPersona(Persona persona);
    Optional<Persona> buscarPersonaPorDni(Long dni);
    List<Persona> listarTodasLasPersonas();

   
    Asistido crearAsistido(Asistido asistido);
    Voluntario crearVoluntario(Voluntario voluntario);
    Optional<Asistido> findAsistidoById(Long id);
    Optional<Voluntario> findVoluntarioById(Long id);
}