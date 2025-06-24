package com.tpdesi.entitys;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.util.List;


@Entity
public class Preparacion {

    private String idPreparacion;

    private LocalDate fechaPreparacion;

    private List<Receta> seleccionDeRecetas;

    private Integer cantidadDeRacionesPreparar;

    public Preparacion() {
    }

    public Preparacion(String idPreparacion, LocalDate fechaPreparacion, List<Receta> seleccionDeRecetas, Integer cantidadDeRacionesPreparar) {
        this.idPreparacion = idPreparacion;
        this.fechaPreparacion = fechaPreparacion;
        this.seleccionDeRecetas = seleccionDeRecetas;
        this.cantidadDeRacionesPreparar = cantidadDeRacionesPreparar;
    }

    public String getIdPreparacion() {
        return idPreparacion;
    }

    public void setIdPreparacion(String idPreparacion) {
        this.idPreparacion = idPreparacion;
    }

    public LocalDate getFechaPreparacion() {
        return fechaPreparacion;
    }

    public void setFechaPreparacion(LocalDate fechaPreparacion) {
        this.fechaPreparacion = fechaPreparacion;
    }

    public List<Receta> getSeleccionDeRecetas() {
        return seleccionDeRecetas;
    }

    public void setSeleccionDeRecetas(List<Receta> seleccionDeRecetas) {
        this.seleccionDeRecetas = seleccionDeRecetas;
    }

    public Integer getCantidadDeRacionesPreparar() {
        return cantidadDeRacionesPreparar;
    }

    public void setCantidadDeRacionesPreparar(Integer cantidadDeRacionesPreparar) {
        this.cantidadDeRacionesPreparar = cantidadDeRacionesPreparar;
    }
}
