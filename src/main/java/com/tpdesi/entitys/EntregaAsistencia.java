package com.tpdesi.entitys;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "entrega_asistencia") 
@Data
@NoArgsConstructor
public class EntregaAsistencia { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "familia_id", nullable = false)
    private Familia familia;

    @ManyToOne
    @JoinColumn(name = "preparacion_id", nullable = false)
    private Preparacion preparacion; 

    @Column(nullable = false)
    private int cantidadRaciones;

    @Column(nullable = false)
    private LocalDate fecha; 

    private boolean activo = true; 
}