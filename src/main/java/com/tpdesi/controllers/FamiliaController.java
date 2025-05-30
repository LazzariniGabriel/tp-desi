package com.tpdesi.controllers;

import com.tpdesi.entitys.Familia;
import com.tpdesi.services.FamiliaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/familias")
public class FamiliaController {

    @Autowired
    private FamiliaService familiaService;

    @PostMapping
    public ResponseEntity<?> crearFamilia(@Valid @RequestBody Familia familia, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage()
                    ));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            Familia nuevaFamilia = familiaService.altaFamilia(familia);
            return new ResponseEntity<>(nuevaFamilia, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor al crear familia: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarFamilia(@PathVariable Long id, @Valid @RequestBody Familia familia, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage()
                    ));
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            Familia familiaActualizada = familiaService.modificarFamilia(id, familia);
            return new ResponseEntity<>(familiaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor al actualizar familia: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFamilia(@PathVariable Long id) {
        try {
            familiaService.bajaFamilia(id);
            return new ResponseEntity<>("Familia eliminada lógicamente con ID: " + id, HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor al eliminar familia: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Familia>> listarFamiliasActivas() {
        List<Familia> familias = familiaService.listarFamiliasActivas();
        return new ResponseEntity<>(familias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Familia> obtenerFamiliaPorId(@PathVariable Long id) {
        Familia familia = familiaService.obtenerFamiliaPorId(id);
        return new ResponseEntity<>(familia, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Familia>> buscarFamilias(
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) String tipoFiltro) {
        List<Familia> familias = familiaService.buscarFamilias(filtro, tipoFiltro);
        return new ResponseEntity<>(familias, HttpStatus.OK);
    }
}