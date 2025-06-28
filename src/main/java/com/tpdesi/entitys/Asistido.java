package com.tpdesi.entitys; 

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tpdesi.model.Persona; 
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "asistido") 
@PrimaryKeyJoinColumn(name = "id") // La PK de esta tabla es también FK de Persona
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Incluye campos de la superclase en equals/hashCode
public class Asistido extends Persona { // Hereda de Persona

    @NotNull(message = "La Ocupación es requerida.")
    @Enumerated(EnumType.STRING)
    private Ocupacion ocupacion;

    private LocalDate fechaRegistro;

    private boolean activo = true; // Para eliminación lógica de asistido en la familia

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familia_id")
    @JsonBackReference
    private Familia familia; 

}