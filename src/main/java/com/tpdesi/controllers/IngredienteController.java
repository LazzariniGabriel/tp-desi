package com.tpdesi.controllers;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.entitys.types.Condimento;
import com.tpdesi.entitys.types.Producto;
import com.tpdesi.services.ingredienteService.IngredienteServiceInterface;
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
@RequestMapping("/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteServiceInterface ingredienteService;

    // ALTA: Ahora puede ser Producto o Condimento
    @PostMapping("/producto")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(getErrorsMap(result), HttpStatus.BAD_REQUEST);
        }
        try {
            Producto nuevo = ingredienteService.crearProducto(producto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/condimento")
    public ResponseEntity<?> crearCondimento(@Valid @RequestBody Condimento condimento, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(getErrorsMap(result), HttpStatus.BAD_REQUEST);
        }
        try {
            Condimento nuevo = ingredienteService.crearCondimento(condimento);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // listará todos los Ingredientes (Producto o Condimento)
    @GetMapping("/listar")
    public ResponseEntity<List<Ingrediente>> listarIngredientes() {
        return new ResponseEntity<>(ingredienteService.listarTodosLosIngredientes(), HttpStatus.OK);
    }

    // Para cualquier tipo de Ingrediente
    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> obtenerIngredientePorId(@PathVariable Long id) {
        Ingrediente ingrediente = ingredienteService.obtenerIngredientePorId(id);
        return new ResponseEntity<>(ingrediente, HttpStatus.OK);
    }

    // Baja Lógica - Aplica a cualquier Ingrediente
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarIngrediente(@PathVariable Long id) {
        try {
            ingredienteService.eliminarIngredienteLogicamente(id);
            return new ResponseEntity<>("Ingrediente eliminado lógicamente con ID: " + id, HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // OPERACIONES DE STOCK (Solo para Productos)
    @PutMapping("/producto/{id}/actualizar-stock")
    public ResponseEntity<?> actualizarStockProducto(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double nuevoStock = body.get("stockDisponible");
            Producto actualizado = ingredienteService.actualizarStockProducto(id, nuevoStock);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/producto/{id}/agregar-stock")
    public ResponseEntity<?> agregarStockProducto(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double cantidad = body.get("cantidad");
            Producto actualizado = ingredienteService.agregarStockProducto(id, cantidad);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/producto/{id}/descontar-stock")
    public ResponseEntity<?> descontarStockProducto(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            Double cantidad = body.get("cantidad");
            Producto actualizado = ingredienteService.descontarStockProducto(id, cantidad);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Helper para mapear errores de validación
    private Map<String, String> getErrorsMap(BindingResult result) {
        return result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}