package com.tpdesi.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_receta") 
@Data
@NoArgsConstructor
public class ItemReceta { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    @JsonBackReference
    @NotNull(message = "El item debe estar asociado a una receta.")
    private Receta receta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingrediente_id")
    @NotNull(message = "El ingrediente del catálogo es requerido.")
    private Ingrediente ingrediente; 

    @NotNull(message = "La cantidad es requerida.")
    @PositiveOrZero(message = "La cantidad debe ser un número positivo o cero.")
    private Double cantidad;

    
    private boolean activo = true;

    
    public ItemReceta(Receta receta, Ingrediente ingrediente, Double cantidad) {
        this.receta = receta;
        this.ingrediente = ingrediente;
        this.cantidad = cantidad;
    }
}