package com.tpdesi.controllers;

import com.tpdesi.DTO.PreparacionListadoDTO; 
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.services.preparacionService.PreparacionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/preparaciones")
public class PreparacionController {

    @Autowired
    private PreparacionServiceInterface preparacionService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarPreparaciones(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "nombreReceta", required = false) String nombreReceta
    ) {
        List<PreparacionListadoDTO> preparaciones = preparacionService.listarPreparacionesActivas(fecha, nombreReceta);
        return ResponseEntity
                .ok(new SuccessResponse("Preparaciones activas obtenidas correctamente", preparaciones));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPreparacion(
            @RequestParam("fechaCoccion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCoccion, 
            @RequestParam("idReceta") Long idReceta,
            @RequestParam("totalRacionesPreparadas") Integer totalRacionesPreparadas 
    ) {
        try {
            Preparacion preparacion = preparacionService.registrarPreparacion(fechaCoccion, idReceta, totalRacionesPreparadas);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessResponse("Preparacion registrada con exito", preparacion));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/dar-baja/{id}")
    public ResponseEntity<?> darBajaPreparacion(@PathVariable("id") Long id) {
        try {
            preparacionService.darBajaPreparacion(id);
            return ResponseEntity
                    .ok(new SuccessResponse("Preparacion dada de baja correctamente. Stock de ingredientes reintegrado.", null));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/modificar-fecha")
    public ResponseEntity<?> modificarFecha(
            @RequestParam("nuevaFechaCoccion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFechaCoccion,
            @RequestParam("id") Long idPreparacion
    ) {
        try {
            Preparacion modificada = preparacionService.modificarDatosDePreparacion(nuevaFechaCoccion, idPreparacion);
            return ResponseEntity
                    .ok(new SuccessResponse("Fecha de la preparacion modificada con exito", modificada));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}