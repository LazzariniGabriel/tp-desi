package com.tpdesi.entitys;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "nombre") // Unicidad por nombre
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del ingrediente es requerido.")
    @Size(max = 100, message = "El nombre del ingrediente no puede exceder los 100 caracteres.")
    @Column(unique = true)
    private String nombre; // Ej: "Harina", "Azúcar", "Leche"

    // Un ingrediente del catálogo siempre está activo por defecto, no se pide eliminación lógica aquí
    private boolean activo = true; // Por si en el futuro se quiere una eliminación lógica de ingredientes del catálogo

    public Ingrediente(String nombre) {
        this.nombre = nombre;
    }
}