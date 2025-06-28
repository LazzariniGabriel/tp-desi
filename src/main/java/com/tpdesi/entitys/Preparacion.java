package com.tpdesi.entitys;

import com.tpdesi.enums.EstadoPreparacion; // Asegúrate que la importación apunte al paquete 'enums'
import jakarta.persistence.*;
import lombok.Data; // Añadido Lombok para consistencia
import lombok.NoArgsConstructor; // Añadido Lombok para consistencia

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

    // Constructor custom si lo necesitas, pero con @Data y @NoArgsConstructor es opcional
    // public Preparacion(Long idPreparacion, LocalDate fechaPreparacion, Receta receta, Integer cantidadDeRacionesPreparar, EstadoPreparacion estadoPreparacion, boolean activa) {
    //     this.idPreparacion = idPreparacion;
    //     this.fechaPreparacion = fechaPreparacion;
    //     this.receta = receta;
    //     this.cantidadDeRacionesPreparar = cantidadDeRacionesPreparar;
    //     this.estadoPreparacion = estadoPreparacion;
    //     this.activa = activa;
    //     this.racionesDisponibles = cantidadDeRacionesPreparar; // Inicializar racionesDisponibles
    // }

    // Si estás usando Lombok @Data, no necesitas los getters y setters explícitos
}