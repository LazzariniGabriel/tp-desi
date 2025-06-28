package com.tpdesi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de herencia
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "dni") // DNI es único entre todas las Personas
public abstract class Persona { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El DNI es requerido.")
    @Positive(message = "El DNI debe ser un valor numérico positivo.")
    @Column(unique = true)
    private Long dni;

    @NotNull(message = "El domicilio es requerido.")
    @Size(min = 1, message = "El domicilio no puede estar vacío.")
    private String domicilio;

    @NotNull(message = "El Apellido es requerido.")
    @Size(min = 1, message = "El Apellido no puede estar vacío.")
    private String apellido;

    @NotNull(message = "El Nombre es requerido.")
    @Size(min = 1, message = "El Nombre no puede estar vacío.")
    private String nombre;

    @NotNull(message = "La Fecha de nacimiento es requerida.")
    @PastOrPresent(message = "La Fecha de nacimiento no puede ser futura.")
    private LocalDate fechaNacimiento;

    
}