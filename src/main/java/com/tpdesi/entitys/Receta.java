package com.tpdesi.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length; 

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "nombre")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El Nombre de la receta es requerido.")
    @Column(unique = true)
    private String nombre;

    @NotBlank(message = "La Descripción de la preparación es requerida.")
    @Column(columnDefinition = "TEXT")
    private String descripcionPreparacion;

    private boolean activa = true;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Valid
    private List<IngredienteReceta> ingredientes = new ArrayList<>();

    public void addIngrediente(IngredienteReceta ingredienteReceta) {
        if (ingredienteReceta != null) {
            ingredientes.add(ingredienteReceta);
            ingredienteReceta.setReceta(this);
        }
    }

    public void removeIngrediente(IngredienteReceta ingredienteReceta) {
        if (ingredienteReceta != null) {
            ingredientes.remove(ingredienteReceta);
            ingredienteReceta.setReceta(null);
        }
    }

    @Transient
    public int getCaloriasTotales() {
        return this.ingredientes.stream()
                .filter(IngredienteReceta::isActivo)
                .mapToInt(IngredienteReceta::getCalorias)
                .sum();
    }
}