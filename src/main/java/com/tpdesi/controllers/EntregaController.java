package com.tpdesi.controllers;

import com.tpdesi.entitys.EntregaAsistencia;
import com.tpdesi.services.EntregaService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/entregas") 
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @PostMapping
    public ResponseEntity<?> registrarEntrega(@RequestParam Long familiaId,
                                              @RequestParam Long preparacionId, 
                                              @RequestParam int cantidadRaciones) { 
        try {
            EntregaAsistencia entrega = entregaService.registrarEntrega(familiaId, preparacionId, cantidadRaciones);
            return new ResponseEntity<>(entrega, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEntrega(@PathVariable Long id) {
        try {
            entregaService.eliminarEntrega(id);
            return new ResponseEntity<>("Entrega de asistencia eliminada correctamente", HttpStatus.NO_CONTENT); // Mensaje actualizado
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al eliminar entrega de asistencia: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<EntregaAsistencia>> listarPorFecha(@RequestParam(required = false) String fecha,
                                                                  @RequestParam(required = false) String nroFamilia,
                                                                  @RequestParam(required = false) String nombreFamilia) {
        LocalDate fechaParseada = (fecha != null) ? LocalDate.parse(fecha) : LocalDate.now();
        List<EntregaAsistencia> entregas = entregaService.buscarPorFiltros(fechaParseada, nroFamilia, nombreFamilia);
        return new ResponseEntity<>(entregas, HttpStatus.OK);
    }
}