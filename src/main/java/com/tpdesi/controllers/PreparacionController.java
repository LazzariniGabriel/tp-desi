package com.tpdesi.controllers;
import com.tpdesi.DTO.RecetaActivaDTO;
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


    @GetMapping("/listar-recetas-activas")
    public ResponseEntity<?> listarRecetasActivas(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "nombre", required = false) String nombre
    ) {
        List<RecetaActivaDTO> recetas = preparacionService.listarRecetasActivas(fecha, nombre);
        return ResponseEntity
                .ok(new SuccessResponse("Recetas activas obtenidas correctamente", recetas));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarPreparacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("idReceta") Long idReceta,
            @RequestParam("cantidad") Integer cantidadRaciones
    ) {
        Preparacion preparacion = preparacionService.registrarPreparacion(fecha, idReceta, cantidadRaciones);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponse("Preparacion registrada con exito", preparacion));
    }

    @PutMapping("/dar-baja/{id}")
    public ResponseEntity<?> darBajaPreparacion(@PathVariable("id") Long idPreparacion) {
        preparacionService.darBajaPreparacion(idPreparacion);
        return ResponseEntity
                .ok(new SuccessResponse("Preparacion dada de baja correctamente", null));
    }

    @PutMapping("/modificar-fecha")
    public ResponseEntity<?> modificarFecha(
            @RequestParam("nuevaFecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nuevaFecha,
            @RequestParam("id") Long idPreparacion
    ) {
        Preparacion modificada = preparacionService.modificarDatosDePreparacion(nuevaFecha, idPreparacion);
        return ResponseEntity
                .ok(new SuccessResponse("Fecha de la preparacion modificada con exito", modificada));
    }


}
