package com.tpdesi.controllers;

import com.tpdesi.entitys.Entrega;
import com.tpdesi.services.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Esta clase es un controlador REST. Atiende las solicitudes HTTP relacionadas a las entregas de alimentos.
@RestController
@RequestMapping("/entregas") // Todas las URLs de este controlador empiezan con /entregas
public class EntregaController {

    // Inyectamos el servicio que contiene la lógica de negocio para trabajar con entregas
    @Autowired
    private EntregaService entregaService;

    /**
     * Endpoint para registrar una entrega de alimentos.
     * Método POST: se usa para crear una nueva entrega.
     * Recibe los parámetros desde la URL o el cuerpo del formulario:
     * - familiaId: ID de la familia que recibe la entrega
     * - recetaId (debería ser preparacionId si se usa la entidad correcta): el plato preparado que se entrega
     * - raciones: cantidad de raciones a entregar
     */
    @PostMapping
    public ResponseEntity<?> registrarEntrega(@RequestParam Long familiaId,
                                              @RequestParam Long recetaId,
                                              @RequestParam int raciones) {
        try {
            // Llama al servicio para registrar la entrega
            Entrega entrega = entregaService.registrarEntrega(familiaId, recetaId, raciones);
            return new ResponseEntity<>(entrega, HttpStatus.CREATED); // 201 si se crea con éxito
        } catch (IllegalArgumentException e) {
            // Si hay un error de validación (familia repetida, más raciones que integrantes, etc.)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    /**
     * Endpoint para eliminar una entrega ya registrada (por error, por ejemplo).
     * Método DELETE: elimina por ID.
     * - id: ID de la entrega a eliminar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEntrega(@PathVariable Long id) {
        try {
            entregaService.eliminarEntrega(id); // Se marca como inactiva y se devuelven las raciones al stock
            return new ResponseEntity<>("Entrega eliminada correctamente", HttpStatus.NO_CONTENT); // 204 sin contenido
        } catch (Exception e) {
            // Si no se encuentra la entrega u ocurre un error, se responde con 404
            return new ResponseEntity<>("Error al eliminar entrega: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para listar las entregas realizadas.
     * Método GET: devuelve la lista de entregas según filtros opcionales.
     * - fecha: permite filtrar por día (si no se indica, usa la fecha actual)
     * - nroFamilia: permite buscar por número de familia (parcial o completo)
     * - nombreFamilia: permite buscar por nombre de familia (parcial o completo)
     */
    @GetMapping
    public ResponseEntity<List<Entrega>> listarPorFecha(@RequestParam(required = false) String fecha,
                                                        @RequestParam(required = false) String nroFamilia,
                                                        @RequestParam(required = false) String nombreFamilia) {
        // Si no se recibe la fecha, se usa la de hoy
        LocalDate fechaParseada = (fecha != null) ? LocalDate.parse(fecha) : LocalDate.now();

        // Se obtiene la lista filtrada desde el servicio
        List<Entrega> entregas = entregaService.buscarPorFiltros(fechaParseada, nroFamilia, nombreFamilia);

        // Se responde con la lista y el código 200 OK
        return new ResponseEntity<>(entregas, HttpStatus.OK);
    }
}
