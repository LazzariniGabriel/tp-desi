package com.tpdesi.entitys;
import com.tpdesi.ENUM.EstadoPreparacion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;


@Entity
public class Preparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idPreparacion;

    private LocalDate fechaPreparacion;

    private List<Receta> seleccionDeRecetas;

    private Integer cantidadDeRacionesPreparar;

    private EstadoPreparacion estadoPreparacion;

    public Preparacion() {
    }

    public Preparacion(String idPreparacion, LocalDate fechaPreparacion, List<Receta> seleccionDeRecetas, Integer cantidadDeRacionesPreparar, EstadoPreparacion estadoPreparacion) {
        this.idPreparacion = idPreparacion;
        this.fechaPreparacion = fechaPreparacion;
        this.seleccionDeRecetas = seleccionDeRecetas;
        this.cantidadDeRacionesPreparar = cantidadDeRacionesPreparar;
        this.estadoPreparacion = estadoPreparacion;
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

    public EstadoPreparacion getEstadoPreparacion() {
        return estadoPreparacion;
    }

    public void setEstadoPreparacion(EstadoPreparacion estadoPreparacion) {
        this.estadoPreparacion = estadoPreparacion;
    }
}
