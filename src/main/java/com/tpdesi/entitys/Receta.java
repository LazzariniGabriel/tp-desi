package com.tpdesi.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length; // <-- Importar esta

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
    // Cambiamos @Size(min = 1, message = "El Nombre de la receta no puede estar vacío.")
    // por @Length(min = 1, message = "El Nombre de la receta no puede estar vacío.")
    // Aunque @NotBlank ya cubre que no sea vacío, si deseas un mensaje específico para "vacío" vs "requerido"
    // o para controlar la longitud máxima, @Length es más flexible sin duplicar claves de error.
    // Para este caso, solo con @NotBlank es suficiente si el mensaje por defecto te sirve.
    // Si quieres el mensaje específico "El Nombre de la receta no puede estar vacío.",
    // puedes dejar solo @Length(min = 1, message = "El Nombre de la receta no puede estar vacío.")
    // y eliminar @NotBlank, o simplemente dejar solo @NotBlank.
    // Para evitar el error de "Duplicate key nombre", elige una de las dos.
    // Recomiendo mantener @NotBlank por ser más concisa para el requisito de "no vacío y no solo espacios".
    // Si realmente necesitas el mensaje "El Nombre de la receta no puede estar vacío." exactamente,
    // y no el de @NotBlank, entonces usa @Length(min = 1, message = "El Nombre de la receta no puede estar vacío.")
    // y quita @NotBlank.
    //
    // Para simplicidad y evitar el error, **opción 1 (la más limpia):**
    // Solo @NotBlank es suficiente para "Todos los datos son requeridos" y "no puede estar vacío".
    //
    // @NotBlank(message = "El Nombre de la receta es requerido.")
    // @Column(unique = true)
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