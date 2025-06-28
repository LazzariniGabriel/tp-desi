package com.tpdesi.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "La Descripción es requerida.")
    @Column(columnDefinition = "TEXT")
    private String descripcion; 

    private boolean activa = true;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Valid
    private List<ItemReceta> items = new ArrayList<>(); 

    public void addItem(ItemReceta itemReceta) {
        if (itemReceta != null) {
            items.add(itemReceta);
            itemReceta.setReceta(this);
        }
    }

    public void removeItem(ItemReceta itemReceta) { 
        if (itemReceta != null) {
            items.remove(itemReceta);
            itemReceta.setReceta(null);
        }
    }

    @Transient
    public int getCaloriasTotales() {
        
        return this.items.stream()
                .filter(ItemReceta::isActivo)
                .mapToInt(item -> item.getIngrediente().getCalorias() * item.getCantidad().intValue()) 
                .sum();
    }
}