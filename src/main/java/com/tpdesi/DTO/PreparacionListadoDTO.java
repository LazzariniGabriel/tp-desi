package com.tpdesi.DTO;

import java.time.LocalDate;

public class PreparacionListadoDTO { 

    private String nombreReceta;
    private LocalDate fechaCoccion; 
    private Integer totalRacionesPreparadas; 
    private Integer caloriasPorPlato;

    public PreparacionListadoDTO(String nombreReceta, LocalDate fechaCoccion, Integer totalRacionesPreparadas, Integer caloriasPorPlato) {
        this.nombreReceta = nombreReceta;
        this.fechaCoccion = fechaCoccion;
        this.totalRacionesPreparadas = totalRacionesPreparadas;
        this.caloriasPorPlato = caloriasPorPlato;
    }

    
    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public LocalDate getFechaCoccion() {
        return fechaCoccion;
    }

    public void setFechaCoccion(LocalDate fechaCoccion) {
        this.fechaCoccion = fechaCoccion;
    }

    public Integer getTotalRacionesPreparadas() {
        return totalRacionesPreparadas;
    }

    public void setTotalRacionesPreparadas(Integer totalRacionesPreparadas) {
        this.totalRacionesPreparadas = totalRacionesPreparadas;
    }

    public Integer getCaloriasPorPlato() {
        return caloriasPorPlato;
    }

    public void setCaloriasPorPlato(Integer caloriasPorPlato) {
        this.caloriasPorPlato = caloriasPorPlato;
    }
}