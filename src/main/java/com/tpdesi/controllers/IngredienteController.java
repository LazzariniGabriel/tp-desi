package com.tpdesi.controllers;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.services.ingredienteService.IngredienteServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteServiceInterface ingredienteService;

    @PostMapping("/agregar")
    public ResponseEntity<?> crearIngrediente(@Valid @RequestBody Ingrediente ingrediente, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(e -> e.getField(), e -> e.getDefaultMessage()));
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }
        try {
            Ingrediente nuevo = ingredienteService.crearIngrediente(ingrediente);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarIngredientes() {
        try {
            return new ResponseEntity<>(ingredienteService.listarIngredientes(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/actualizarstock/{id}")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double nuevaCantidad = body.get("cantidadEnStock");
            Ingrediente actualizado = ingredienteService.actualizarStock(id, nuevaCantidad);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/agregar-stock")
    public ResponseEntity<?> agregarStock(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double peso = body.get("pesoEnKG");
            Ingrediente actualizado = ingredienteService.agregarStock(id, peso);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/descontar-stock")
    public ResponseEntity<?> descontarStock(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double peso = body.get("pesoEnKG");
            Ingrediente actualizado = ingredienteService.decrementarStock(id, peso);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/verificar-stock")
    public ResponseEntity<?> verificarStock(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double peso = body.get("peso");
            boolean disponible = ingredienteService.verificarDisponibilidadStock(id, peso);
            return new ResponseEntity<>(Map.of("disponible", disponible), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarIngrediente(@PathVariable Long id) {
        try {
            ingredienteService.eliminarIngrediente(id);
            return new ResponseEntity<>("Ingrediente eliminado", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
