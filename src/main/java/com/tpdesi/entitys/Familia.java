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
    private String nombreFamilia;

    private LocalDate fechaAlta = LocalDate.now();

    private LocalDate fechaUltimaAsistenciaRecibida;

    private boolean activa = true;

    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Valid
    private List<Integrante> integrantes = new ArrayList<>();

    public void addIntegrante(Integrante integrante) {
        if (integrante != null) {
            integrantes.add(integrante);
            integrante.setFamilia(this);
        }
    }

    public void removeIntegrante(Integrante integrante) {
        if (integrante != null) {
            integrantes.remove(integrante);
            integrante.setFamilia(null);
        }
    }

    public long getCantidadIntegrantesActivos() {
        return this.integrantes.stream()
                .filter(Integrante::isActivo)
                .count();
    }
}