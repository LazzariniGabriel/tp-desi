package com.tpdesi.entitys;

import com.fasterxml.jackson.annotation.JsonBackReference; // Para manejar la relación bidireccional
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero; // La cantidad puede ser 0.0 si se desea

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class IngredienteReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con Receta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    @JsonBackReference // Evita bucle infinito
    @NotNull(message = "El ingrediente debe estar asociado a una receta.")
    private Receta receta;

    // Relación ManyToOne con Ingrediente (el catálogo)
    @ManyToOne(fetch = FetchType.EAGER) // EAGER para que se cargue el nombre del ingrediente
    @JoinColumn(name = "ingrediente_id")
    @NotNull(message = "El ingrediente del catálogo es requerido.")
    private Ingrediente ingrediente; // Referencia al ingrediente del catálogo

    @NotNull(message = "La cantidad es requerida.")
    @PositiveOrZero(message = "La cantidad debe ser un número positivo o cero.")
    private Double cantidad; // Notar que no se tendrá en cuenta la unidad de medida, ya que se supone que todo está expresado en kg

    @NotNull(message = "Las calorías son requeridas.")
    @Positive(message = "Las calorías deben ser un número entero positivo.")
    private Integer calorias; // Numero entero positivo, representa la cantidad de calorías que aporta a la preparación

    private boolean activo = true; // Para eliminación lógica de ingredientes de una receta

    public IngredienteReceta(Receta receta, Ingrediente ingrediente, Double cantidad, Integer calorias) {
        this.receta = receta;
        this.ingrediente = ingrediente;
        this.cantidad = cantidad;
        this.calorias = calorias;
    }
}