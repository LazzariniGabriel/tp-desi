package com.tpdesi.entitys;

// Importaciones necesarias para trabajar con JPA (mapear la clase a la base de datos)
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

// Esta clase representa la entidad Entrega, que será una tabla en la base de datos
@Entity
@Data // Lombok: genera automáticamente getters, setters, toString, equals, etc.
@NoArgsConstructor // Lombok: genera un constructor vacío
public class Entrega {

    // ID autogenerado que funciona como clave primaria en la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la entidad Familia (una entrega está asociada a una familia)
    @ManyToOne
    @JoinColumn(name = "familia_id", nullable = false) // Nombre de la columna que se crea en la tabla entrega
    private Familia familia;

    // Relación con la entidad Receta (aunque en el UML debería ser Preparacion)
    @ManyToOne
    @JoinColumn(name = "receta_id", nullable = false) // La receta/preparación entregada
    private Receta receta;

    // Cantidad de raciones entregadas (no puede ser nulo)
    @Column(nullable = false)
    private int cantidadRaciones;

    // Fecha de entrega. Se carga automáticamente con la fecha actual al crear la entrega
    @Column(nullable = false)
    private LocalDate fechaEntrega = LocalDate.now();

    // Campo lógico para saber si la entrega está activa (true) o fue eliminada (false)
    private boolean activo = true;
}
