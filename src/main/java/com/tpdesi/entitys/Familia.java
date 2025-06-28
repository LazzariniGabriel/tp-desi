package com.tpdesi.entitys;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "nroFamilia")
public class Familia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nroFamilia; 

    @NotNull(message = "El Nombre de Familia es requerido.")
    @Size(min = 1, message = "El Nombre de Familia no puede estar vacío.")
    private String nombre; 

    private LocalDate fechaRegistro = LocalDate.now(); 

    private LocalDate fechaUltimaAsistenciaRecibida; 

    private boolean activa = true; 

    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Valid
    private List<Asistido> integrantes = new ArrayList<>(); 

    public void addIntegrante(Asistido asistido) { 
        if (asistido != null) {
            integrantes.add(asistido);
            asistido.setFamilia(this);
        }
    }

    public void removeIntegrante(Asistido asistido) { 
        if (asistido != null) {
            integrantes.remove(asistido);
            asistido.setFamilia(null);
        }
    }

    public long getCantidadIntegrantesActivos() {
        return this.integrantes.stream()
                .filter(Asistido::isActivo) 
                .count();
    }
}