package com.tpdesi.controllers;

import com.tpdesi.entitys.Receta;
import com.tpdesi.services.RecetaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearReceta(@Valid @RequestBody Receta receta, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(getErrorsMap(result), HttpStatus.BAD_REQUEST);
        }
        try {
            Receta nuevaReceta = recetaService.altaReceta(receta);
            return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor al crear receta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReceta(@PathVariable Long id, @Valid @RequestBody Receta receta, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(getErrorsMap(result), HttpStatus.BAD_REQUEST);
        }
        try {
            Receta recetaActualizada = recetaService.modificarReceta(id, receta);
            return new ResponseEntity<>(recetaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor al actualizar receta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReceta(@PathVariable Long id) {
        try {
            recetaService.bajaReceta(id);
            return new ResponseEntity<>("Receta eliminada lógicamente con ID: " + id, HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor al eliminar receta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Receta>> listarRecetasActivas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Double caloriasMin,
            @RequestParam(required = false) Double caloriasMax) {
        List<Receta> recetas = recetaService.buscarRecetas(nombre, caloriasMin, caloriasMax);
        return new ResponseEntity<>(recetas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receta> obtenerRecetaPorId(@PathVariable Long id) {
        Receta receta = recetaService.obtenerRecetaPorId(id);
        return new ResponseEntity<>(receta, HttpStatus.OK);
    }

    private Map<String, String> getErrorsMap(BindingResult result) {
        return result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}