package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.RecetaActivaDTO;
import com.tpdesi.enums.EstadoPreparacion;
import com.tpdesi.entitys.IngredienteReceta;
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.entitys.Receta;
import com.tpdesi.exceptionHandler.EstadoInvalidoException;
import com.tpdesi.exceptionHandler.FechaInvalidaException;
import com.tpdesi.exceptionHandler.PreparacionDuplicadaException;
import com.tpdesi.exceptionHandler.StockInsuficienteException;
import com.tpdesi.repositorys.PreparacionRepository;
import com.tpdesi.services.RecetaService; // Tu servicio de Receta
import com.tpdesi.services.ingredienteService.IngredienteServiceImpl; // Tu servicio de Ingredientes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importado para transacciones

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Importado

@Service
public class PreparacionServiceImpl implements PreparacionServiceInterface {

    @Autowired
    private PreparacionRepository preparacionRepository;

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private IngredienteServiceImpl ingredienteService;

    @Override
    @Transactional // Asegura que toda la operación sea atómica
    public Preparacion registrarPreparacion(LocalDate fecha, Long idReceta, Integer cantidadRaciones) {
        try {
            // VALIDACIÓN DE FECHA: "no sea futura"
            if (fecha.isAfter(LocalDate.now())) {
                throw new FechaInvalidaException("La fecha de preparación no puede ser futura.");
            }

            // Validar que la receta exista y esté activa
            Receta receta = recetaService.obtenerRecetaPorId(idReceta); // Usa tu método para obtener receta
            if (!receta.isActiva()) {
                throw new EstadoInvalidoException("La receta seleccionada no está activa.");
            }

            // VALIDACIÓN DE DUPLICIDAD: "No podrá haber dos preparaciones de la misma receta para el mismo día"
            Optional<Preparacion> preparacionExistente = preparacionRepository.findByReceta_IdAndFechaPreparacion(idReceta, fecha);
            if (preparacionExistente.isPresent()) {
                throw new PreparacionDuplicadaException("Ya existe una preparación con esta receta para la fecha indicada.");
            }

            // VALIDACIÓN DE STOCK DE INGREDIENTES: verificar disponibilidad antes de descontar
            for (IngredienteReceta ir : receta.getIngredientes()) {
                if (!ir.isActivo()) continue; // Solo considerar ingredientes activos de la receta

                Long idIngrediente = ir.getIngrediente().getId();
                Double cantidadTotalNecesaria = ir.getCantidad() * cantidadRaciones;

                // Verificar stock sin descontar aún
                Double stockActual = ingredienteService.obtenerStock(idIngrediente);
                if (stockActual < cantidadTotalNecesaria) {
                    double faltante = cantidadTotalNecesaria - stockActual;
                    throw new StockInsuficienteException(
                            "Stock insuficiente para: " + ir.getIngrediente().getNombre() +
                                    ". Se necesitan " + String.format("%.2f", cantidadTotalNecesaria) + " KG, hay " +
                                    String.format("%.2f", stockActual) + " KG. Faltan " +
                                    String.format("%.2f", faltante) + " KG."
                    );
                }
            }

            // DESCUENTO DE STOCK DE INGREDIENTES: Si todas las validaciones pasan, se procede a descontar
            for (IngredienteReceta ir : receta.getIngredientes()) {
                if (!ir.isActivo()) continue; // Solo descontar de ingredientes activos

                Long idIngrediente = ir.getIngrediente().getId();
                Double cantidadTotalADescontar = ir.getCantidad() * cantidadRaciones;
                ingredienteService.decrementarStock(idIngrediente, cantidadTotalADescontar);
            }

            // Creación y guardado de la Preparacion
            Preparacion nuevaPreparacion = new Preparacion();
            nuevaPreparacion.setFechaPreparacion(fecha);
            nuevaPreparacion.setReceta(receta); // Usa el campo único 'receta'
            nuevaPreparacion.setCantidadDeRacionesPreparar(cantidadRaciones);
            nuevaPreparacion.setRacionesDisponibles(cantidadRaciones); // Inicializa racionesDisponibles con el total preparado
            nuevaPreparacion.setEstadoPreparacion(EstadoPreparacion.FINALIZADA); // Asignado a FINALIZADA como en tu comentario
            nuevaPreparacion.setActiva(true); // Usa el campo 'activa'

            return preparacionRepository.save(nuevaPreparacion);

        } catch (RuntimeException e) { // Captura las excepciones de negocio específicas
            // Relanza las excepciones de negocio para que sean manejadas por GlobalExceptionHandler
            if (e instanceof FechaInvalidaException || e instanceof EstadoInvalidoException ||
                e instanceof PreparacionDuplicadaException || e instanceof StockInsuficienteException) {
                throw e;
            }
            throw new RuntimeException("Error al registrar la preparación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional // Asegura que la operación de reintegro sea atómica
    public void darBajaPreparacion(Long idPreparacion) {
        try {
            Preparacion preparacion = preparacionRepository.findById(idPreparacion)
                    .orElseThrow(() -> new RuntimeException("Preparación no encontrada con id: " + idPreparacion));

            if (!preparacion.isActiva()) { // Evitar reintegrar si ya estaba de baja
                throw new IllegalArgumentException("La preparación con ID " + idPreparacion + " ya se encuentra dada de baja.");
            }

            // REINTEGRO DE STOCK DE INGREDIENTES (CRÍTICO)
            Receta recetaAsociada = preparacion.getReceta();
            Integer racionesPreparadasOriginales = preparacion.getCantidadDeRacionesPreparar();

            for (IngredienteReceta ir : recetaAsociada.getIngredientes()) {
                if (!ir.isActivo()) continue; // Solo reintegrar ingredientes activos de la receta

                Long idIngrediente = ir.getIngrediente().getId();
                Double cantidadAIncrementar = ir.getCantidad() * racionesPreparadasOriginales;
                ingredienteService.agregarStock(idIngrediente, cantidadAIncrementar);
            }

            // Marcar preparación como inactiva
            preparacion.setActiva(false); // Usa el campo 'activa'
            preparacionRepository.save(preparacion);

        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) { // Relanza para manejar errores de negocio específicos
                throw e;
            }
            throw new RuntimeException("Error al dar de baja la preparación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional // Asegura que la operación sea atómica
    public Preparacion modificarDatosDePreparacion(LocalDate nuevaFecha, Long idPreparacion) {
        try {
            Preparacion preparacion = preparacionRepository.findById(idPreparacion)
                    .orElseThrow(() -> new RuntimeException("Preparación no encontrada con id: " + idPreparacion));

            // VALIDACIÓN DE FECHA: "La nueva fecha no puede ser futura"
            if (nuevaFecha.isAfter(LocalDate.now())) {
                throw new FechaInvalidaException("La nueva fecha de preparación no puede ser futura.");
            }
            
            // Re-validar duplicidad si la fecha cambia
            if (!preparacion.getFechaPreparacion().equals(nuevaFecha)) {
                Optional<Preparacion> preparacionExistente = preparacionRepository.findByReceta_IdAndFechaPreparacion(
                    preparacion.getReceta().getId(), nuevaFecha);
                if (preparacionExistente.isPresent() && !preparacionExistente.get().getIdPreparacion().equals(idPreparacion)) {
                    throw new PreparacionDuplicadaException("Ya existe otra preparación con la misma receta para la nueva fecha.");
                }
            }


            // Criterio: Solo se podrá editar la fecha de la preparación, no se puede cambiar la receta ni la cantidad de raciones
            preparacion.setFechaPreparacion(nuevaFecha);
            return preparacionRepository.save(preparacion);

        } catch (RuntimeException e) {
             if (e instanceof FechaInvalidaException || e instanceof PreparacionDuplicadaException) {
                throw e;
            }
            throw new RuntimeException("Error al modificar la fecha de la preparación: " + e.getMessage(), e);
        }
    }

    @Override
    // RENOMBRADO: listarPreparacionesActivas para mayor claridad
    public List<RecetaActivaDTO> listarPreparacionesActivas(LocalDate fechaFiltrar, String nombreRecetaFiltrar) {
        try {
            List<Preparacion> preparaciones;

            // Lógica de filtrado con los nuevos métodos del repositorio
            if (fechaFiltrar != null && nombreRecetaFiltrar != null && !nombreRecetaFiltrar.trim().isEmpty()) {
                preparaciones = preparacionRepository.findByFechaPreparacionAndRecetaNombreContainingIgnoreCaseAndActivaTrue(fechaFiltrar, nombreRecetaFiltrar);
            } else if (fechaFiltrar != null) {
                preparaciones = preparacionRepository.findByFechaPreparacionAndActivaTrue(fechaFiltrar);
            } else if (nombreRecetaFiltrar != null && !nombreRecetaFiltrar.trim().isEmpty()) {
                preparaciones = preparacionRepository.findByRecetaNombreContainingIgnoreCaseAndActivaTrue(nombreRecetaFiltrar);
            } else {
                preparaciones = preparacionRepository.findByActivaTrue(); // Solo listar preparaciones activas
            }

            // Mapeo a DTO para la salida requerida
            return preparaciones.stream()
                    .map(p -> {
                        // Calcular calorías por plato para el DTO
                        int caloriasPorPlato = p.getReceta().getIngredientes().stream()
                                .filter(IngredienteReceta::isActivo)
                                .mapToInt(IngredienteReceta::getCalorias)
                                .sum();

                        return new RecetaActivaDTO(
                                p.getReceta().getNombre(), // Nombre de la receta asociada a la preparación
                                p.getFechaPreparacion(),
                                p.getCantidadDeRacionesPreparar(),
                                caloriasPorPlato
                        );
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar preparaciones activas: " + e.getMessage(), e);
        }
    }
}