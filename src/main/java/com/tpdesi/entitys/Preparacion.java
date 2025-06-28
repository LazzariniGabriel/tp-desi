package com.tpdesi.entitys;

import com.tpdesi.enums.EstadoPreparacion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Preparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private LocalDate fechaCoccion;

    @ManyToOne
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta; 

    private Integer totalRacionesPreparadas;

    private Integer stockRacionesRestantes; 

    private EstadoPreparacion estadoPreparacion; 

    private boolean activa; 

   
}