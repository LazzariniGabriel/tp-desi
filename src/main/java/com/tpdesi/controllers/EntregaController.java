package com.tpdesi.controllers;

import com.tpdesi.entitys.Entrega;
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
                                              @RequestParam Long recetaId,
                                              @RequestParam int raciones) {
        try {
            Entrega entrega = entregaService.registrarEntrega(familiaId, recetaId, raciones);
            return new ResponseEntity<>(entrega, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEntrega(@PathVariable Long id) {
        try {
            entregaService.eliminarEntrega(id);
            return new ResponseEntity<>("Entrega eliminada correctamente", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar entrega: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Entrega>> listarPorFecha(@RequestParam(required = false) String fecha,
                                                        @RequestParam(required = false) String nroFamilia,
                                                        @RequestParam(required = false) String nombreFamilia) {
        LocalDate fechaParseada = (fecha != null) ? LocalDate.parse(fecha) : LocalDate.now();
        List<Entrega> entregas = entregaService.buscarPorFiltros(fechaParseada, nroFamilia, nombreFamilia);
        return new ResponseEntity<>(entregas, HttpStatus.OK);
    }
}
