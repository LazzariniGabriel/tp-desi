package com.tpdesi.entitys;

import com.tpdesi.enums.EstadoPreparacion;
import jakarta.persistence.*;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import java.time.LocalDate;

@Entity
@Data // Genera getters, setters, equals, hashCode y toString
@NoArgsConstructor // Genera constructor sin argumentos
public class Preparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPreparacion;

    private LocalDate fechaPreparacion;

    // CAMBIO CRÍTICO: Relación de ManyToMany a ManyToOne con Receta
    @ManyToOne
    @JoinColumn(name = "receta_id", nullable = false) // Una Preparacion está ligada a UNA Receta
    private Receta receta; // Cambiado de 'seleccionDeRecetas' (List) a una única 'receta'

    private Integer cantidadDeRacionesPreparar;

    // NUEVO CAMPO CRÍTICO: Stock de raciones disponibles para esta preparación
    private Integer racionesDisponibles; // Agregado para controlar el stock de raciones

    private EstadoPreparacion estadoPreparacion;

    // CAMBIO CRÍTICO: Renombrado de 'recetaActiva' a 'activa' para la eliminación lógica de la Preparacion
    private boolean activa; // Renombrado de 'recetaActiva'

    
}