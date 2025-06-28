package com.tpdesi.entitys;
import com.tpdesi.ENUM.EstadoPreparacion;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Preparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPreparacion;

    private LocalDate fechaPreparacion;

    @ManyToMany
    @JoinTable(
            name = "preparacion_receta",
            joinColumns = @JoinColumn(name = "preparacion_id"),
            inverseJoinColumns = @JoinColumn(name = "receta_id")
    )
    private List<Receta> seleccionDeRecetas = new ArrayList<>();


    private Integer cantidadDeRacionesPreparar;

    private EstadoPreparacion estadoPreparacion;

    private Boolean recetaActiva;

    public Preparacion() {
    }

    public Preparacion(Long idPreparacion, LocalDate fechaPreparacion, List<Receta> seleccionDeRecetas, Integer cantidadDeRacionesPreparar, EstadoPreparacion estadoPreparacion, Boolean recetaActiva) {
        this.idPreparacion = idPreparacion;
        this.fechaPreparacion = fechaPreparacion;
        this.seleccionDeRecetas = seleccionDeRecetas;
        this.cantidadDeRacionesPreparar = cantidadDeRacionesPreparar;
        this.estadoPreparacion = estadoPreparacion;
        this.recetaActiva = recetaActiva;
    }

    public Long getIdPreparacion() {
        return idPreparacion;
    }

    public void setIdPreparacion(Long idPreparacion) {
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

    public Boolean getRecetaActiva() {
        return recetaActiva;
    }

    public void setRecetaActiva(Boolean recetaActiva) {
        this.recetaActiva = recetaActiva;
    }
}
