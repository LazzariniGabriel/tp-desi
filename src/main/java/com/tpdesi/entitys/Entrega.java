package com.tpdesi.entitys;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "familia_id", nullable = false)
    private Familia familia;

    // CAMBIO CRÍTICO: Ahora la entrega se relaciona con una Preparacion, no directamente con una Receta
    @ManyToOne
    @JoinColumn(name = "preparacion_id", nullable = false) // Columna para la clave foránea de Preparacion
    private Preparacion preparacion; // Cambiado de 'receta' a 'preparacion'

    @Column(nullable = false)
    private int cantidadRaciones;

    @Column(nullable = false)
    private LocalDate fechaEntrega = LocalDate.now();

    private boolean activo = true; // Para eliminación lógica
}