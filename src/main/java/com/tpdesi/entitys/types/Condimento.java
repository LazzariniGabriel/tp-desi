package com.tpdesi.entitys.types; // 'types' para los tipos de ingrediente

import com.tpdesi.entitys.Ingrediente; 
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "condimento") 
@PrimaryKeyJoinColumn(name = "id") // La PK de esta tabla es también FK de Ingrediente
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Condimento extends Ingrediente { // Hereda de Ingrediente
    
}