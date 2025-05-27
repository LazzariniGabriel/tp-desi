package com.tpdesi.entitys;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Integrante {

    @Id
    private Long dni;

    private String apellido;
    private String nombre;
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Ocupacion ocupacion;

    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

    public enum Ocupacion {
        DESEMPLEADO, EMPLEADO, ESTUDIANTE, AMA_DE_CASA, OTRO
    }

    // Getters y Setters
}
