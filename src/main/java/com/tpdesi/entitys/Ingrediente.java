package com.tpdesi.entitys;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; 
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "nombre") 
public abstract class Ingrediente { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del ingrediente es requerido.")
    @Size(max = 100, message = "El nombre del ingrediente no puede exceder los 100 caracteres.")
    @Column(unique = true)
    private String nombre;

    @NotNull(message = "Las calorías son requeridas.")
    @PositiveOrZero(message = "Las calorías deben ser un número positivo o cero.") 
    private Integer calorias; 
    private boolean activo = true; 
}