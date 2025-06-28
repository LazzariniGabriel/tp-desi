package com.tpdesi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voluntario") 
@PrimaryKeyJoinColumn(name = "id") // La PK de esta tabla es también FK de Persona
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Incluye campos de la superclase en equals/hashCode
public class Voluntario extends Persona { 

    private String nroSegu;
}