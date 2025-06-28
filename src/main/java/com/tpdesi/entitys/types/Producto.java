
package com.tpdesi.entitys.types; // 'types' para los tipos de ingrediente

import com.tpdesi.entitys.Ingrediente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto") 
@PrimaryKeyJoinColumn(name = "id") // La PK de esta tabla es también FK de Ingrediente
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Producto extends Ingrediente { // Hereda de Ingrediente

    @PositiveOrZero(message = "El stock disponible debe ser un número positivo o cero.")
    private Double stockDisponible; 

    @PositiveOrZero(message = "El precio actual debe ser un número positivo o cero.")
    @Column(columnDefinition = "FLOAT DEFAULT 0.0")
    private Float precioActual;
}