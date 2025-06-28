package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.RecetaActivaDTO;
import com.tpdesi.entitys.Preparacion;

import java.time.LocalDate;
import java.util.List;

public interface PreparacionServiceInterface {

    // En este método se debe aplicar una validación, que según las raciones requeridas se verifique que se cuente con
    // el stock y ademas que en la fecha ingresada no exista una preparación con la misma receta el mismo día.
    // Validar que la fecha sea valida (no adelantada).
    Preparacion registrarPreparacion (LocalDate fecha, Long idReceta, Integer cantidadRaciones); // Ajustado nombre de parámetro 'fechaValida' a 'fecha' para consistencia

    // Esta será una eliminación lógica, no de la entidad persistida. También debe reintegrar el stock de ingredientes.
    void darBajaPreparacion(Long idPreparacion);

    // Solo se permite modificar la fecha de la preparación.
    Preparacion modificarDatosDePreparacion(LocalDate nuevaFecha, Long idPreparacion);

    // Para el filtro en este método se usaran métodos Query de Spring data JPA en la clase Preparacion Repository
    // RENOMBRADO: listarPreparacionesActivas
    List<RecetaActivaDTO> listarPreparacionesActivas(LocalDate fechaFiltrar, String nombreRecetaFiltrar); // Ajustado nombre de parámetro 'nombre' a 'nombreRecetaFiltrar' para claridad
}