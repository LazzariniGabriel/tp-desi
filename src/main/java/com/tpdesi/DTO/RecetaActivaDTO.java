package com.tpdesi.DTO;

import java.time.LocalDate;

public class RecetaActivaDTO {

    private String nombreReceta;
    private LocalDate fechaDePreparacion;
    private Integer numeroDeRacionesPreparadas;
    private Integer caloriasPorPlato;

    public RecetaActivaDTO(String nombreReceta, LocalDate fechaDePreparacion, Integer numeroDeRacionesPreparadas, Integer caloriasPorPlato) {
        this.nombreReceta = nombreReceta;
        this.fechaDePreparacion = fechaDePreparacion;
        this.numeroDeRacionesPreparadas = numeroDeRacionesPreparadas;
        this.caloriasPorPlato = caloriasPorPlato;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public LocalDate getFechaDePreparacion() {
        return fechaDePreparacion;
    }

    public void setFechaDePreparacion(LocalDate fechaDePreparacion) {
        this.fechaDePreparacion = fechaDePreparacion;
    }

    public Integer getNumeroDeRacionesPreparadas() {
        return numeroDeRacionesPreparadas;
    }

    public void setNumeroDeRacionesPreparadas(Integer numeroDeRacionesPreparadas) {
        this.numeroDeRacionesPreparadas = numeroDeRacionesPreparadas;
    }

    public Integer getCaloriasPorPlato() {
        return caloriasPorPlato;
    }

    public void voidSetCaloriasPorPlato(Integer caloriasPorPlato) { // Corregido typo en setCaloriasPorPlato
        this.caloriasPorPlato = caloriasPorPlato;
    }
}