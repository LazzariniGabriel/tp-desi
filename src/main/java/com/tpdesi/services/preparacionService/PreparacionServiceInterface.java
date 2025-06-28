package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.RecetaActivaDTO;
import com.tpdesi.entitys.Preparacion;

import java.time.LocalDate;
import java.util.List;

public interface PreparacionServiceInterface {

//    en este metodo se debe aplicar una validacion, que segun las raciones requeridas se verifique que se cuente con
//    el stock y ademas que en la fecha ingresada no exista una preparacion con la misma receta el mismo dia.
//    Validar que la fecha sea valida (no adelantada).
    Preparacion registrarPreparacion (LocalDate fechaValida, Long idReceta, Integer cantidadRaciones);

//    Esta sera una eliminacion logica, no de la entidad persistida.
    void darBajaPreparacion(Long idPreparacion);

//Solo se permite modificar la fecha de la preparacion.
    Preparacion modificarDatosDePreparacion(LocalDate nuevaFecha, Long idPreparacion);


//    Para el filtro en este metodo se usaran metodos Query de Spring data JPA en la clase Preparacion Repository
    List<RecetaActivaDTO> listarRecetasActivas(LocalDate fechaFiltrar, String nombreFiltrar);



















}
