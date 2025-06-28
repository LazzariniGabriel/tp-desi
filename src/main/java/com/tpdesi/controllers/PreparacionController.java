package com.tpdesi.controllers;
import com.tpdesi.DTO.RecetaActivaDTO; // Este DTO debería llamarse algo como PreparacionDTO o PreparacionListadoDTO
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.services.preparacionService.PreparacionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/preparaciones")
public class PreparacionController {

    @Autowired
    private PreparacionServiceInterface preparacionService;


    // CAMBIO DE NOMBRE DE ENDPOINT: Más claro para listar preparaciones
    @GetMapping("/listar") // Cambiado de /listar-recetas-activas a /listar
    public ResponseEntity<?> listarPreparaciones( // Cambiado de listarRecetasActivas a listarPreparaciones
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "nombreReceta", required = false) String nombreReceta // Cambiado de 'nombre' a 'nombreReceta' para claridad
    ) {
        // Asumiendo que el DTO RecetaActivaDTO se usa para representar la salida del listado de preparaciones
        List<RecetaActivaDTO> preparaciones = preparacionService.listarPreparacionesActivas(fecha, nombreReceta); // Ajustado nombre de método
        return ResponseEntity
                .ok(new SuccessResponse("Preparaciones activas obtenidas correctamente", preparaciones));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPreparacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idReceta") Long idReceta,
            @RequestParam("cantidad") Integer cantidadRaciones
    ) {
        try {
            Preparacion preparacion = preparacionService.registrarPreparacion(fecha, idReceta, cantidadRaciones);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessResponse("Preparacion registrada con exito", preparacion));
        } catch (IllegalArgumentException e) { // Capturar excepciones de validación específicas
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) { // Catch all para otras excepciones del servicio
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/dar-baja/{id}")
    public ResponseEntity<?> darBajaPreparacion(@PathVariable("id") Long idPreparacion) {
        try {
            preparacionService.darBajaPreparacion(idPreparacion);
            return ResponseEntity
                    .ok(new SuccessResponse("Preparacion dada de baja correctamente. Stock reintegrado.", null)); // Mensaje actualizado
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // O 400 si es un error de lógica de negocio
        }
    }

    @PutMapping("/modificar-fecha")
    public ResponseEntity<?> modificarFecha(
            @RequestParam("nuevaFecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFecha,
            @RequestParam("id") Long idPreparacion
    ) {
        try {
            Preparacion modificada = preparacionService.modificarDatosDePreparacion(nuevaFecha, idPreparacion);
            return ResponseEntity
                    .ok(new SuccessResponse("Fecha de la preparacion modificada con exito", modificada));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}