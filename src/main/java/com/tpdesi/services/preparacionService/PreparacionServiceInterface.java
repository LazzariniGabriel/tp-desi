package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.PreparacionListadoDTO;
import com.tpdesi.entitys.Preparacion;

import java.time.LocalDate;
import java.util.List;

public interface PreparacionServiceInterface {

   
    // Validar que la fecha sea valida (no adelantada).
    Preparacion registrarPreparacion (LocalDate fecha, Long idReceta, Integer cantidadRaciones); 

    // Esta será una eliminación lógica, no de la entidad persistida. También debe reintegrar el stock de ingredientes.
    void darBajaPreparacion(Long idPreparacion);

    // Solo se permite modificar la fecha de la preparación.
    Preparacion modificarDatosDePreparacion(LocalDate nuevaFecha, Long idPreparacion);
    
    List<PreparacionListadoDTO> listarPreparacionesActivas(LocalDate fechaFiltrar, String nombreRecetaFiltrar); 
}