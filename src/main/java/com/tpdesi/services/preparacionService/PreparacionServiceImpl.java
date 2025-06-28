package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.PreparacionListadoDTO;
import com.tpdesi.enums.EstadoPreparacion;
import com.tpdesi.entitys.ItemReceta; 
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.entitys.Receta;
import com.tpdesi.entitys.types.Producto;
import com.tpdesi.exceptionHandler.EstadoInvalidoException;
import com.tpdesi.exceptionHandler.FechaInvalidaException;
import com.tpdesi.exceptionHandler.PreparacionDuplicadaException;
import com.tpdesi.exceptionHandler.StockInsuficienteException;
import com.tpdesi.repositorys.PreparacionRepository;
import com.tpdesi.services.RecetaService;
import com.tpdesi.services.ingredienteService.IngredienteServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PreparacionServiceImpl implements PreparacionServiceInterface {

    @Autowired
    private PreparacionRepository preparacionRepository;

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private IngredienteServiceImpl ingredienteService;

    @Override
    @Transactional
    public Preparacion registrarPreparacion(LocalDate fechaCoccion, Long idReceta, Integer totalRacionesPreparadas) { 
        try {
            if (fechaCoccion.isAfter(LocalDate.now())) {
                throw new FechaInvalidaException("La fecha de preparación no puede ser futura.");
            }

            Receta receta = recetaService.obtenerRecetaPorId(idReceta);
            if (!receta.isActiva()) {
                throw new EstadoInvalidoException("La receta seleccionada no está activa.");
            }

            Optional<Preparacion> preparacionExistente = preparacionRepository.findByReceta_IdAndFechaCoccion(idReceta, fechaCoccion); 
            if (preparacionExistente.isPresent()) {
                throw new PreparacionDuplicadaException("Ya existe una preparación con esta receta para la fecha indicada.");
            }

            // VALIDACIÓN DE STOCK DE INGREDIENTES
            for (ItemReceta item : receta.getItems()) { 
                if (!item.isActivo()) continue;

                // Solo productos tienen stock. Si es Condimento, no se verifica stock aquí.
                if (!(item.getIngrediente() instanceof Producto)) {
                    // Si el ingrediente no es un Producto (ej. es un Condimento), no tiene stock gestionable de esta forma.
                    continue; 
                }
                
                Producto producto = (Producto) item.getIngrediente(); // Castea a Producto
                Double cantidadTotalNecesaria = item.getCantidad() * totalRacionesPreparadas;

                Double stockActual = ingredienteService.obtenerStockProducto(producto.getId()); // Obtener stock de Producto
                if (stockActual < cantidadTotalNecesaria) {
                    double faltante = cantidadTotalNecesaria - stockActual;
                    throw new StockInsuficienteException(
                            "Stock insuficiente para: " + producto.getNombre() + // Usa nombre del producto
                                    ". Se necesitan " + String.format("%.2f", cantidadTotalNecesaria) + " KG, hay " +
                                    String.format("%.2f", stockActual) + " KG. Faltan " +
                                    String.format("%.2f", faltante) + " KG."
                    );
                }
            }

            // DESCUENTO DE STOCK DE INGREDIENTES
            for (ItemReceta item : receta.getItems()) { 
                if (!item.isActivo()) continue;

                if (!(item.getIngrediente() instanceof Producto)) {
                    continue; 
                }

                Producto producto = (Producto) item.getIngrediente(); // Castea a Producto
                Double cantidadTotalADescontar = item.getCantidad() * totalRacionesPreparadas;
                ingredienteService.descontarStockProducto(producto.getId(), cantidadTotalADescontar); // Descuenta de Producto
            }

            Preparacion nuevaPreparacion = new Preparacion();
            nuevaPreparacion.setFechaCoccion(fechaCoccion); 
            nuevaPreparacion.setReceta(receta);
            nuevaPreparacion.setTotalRacionesPreparadas(totalRacionesPreparadas); 
            nuevaPreparacion.setStockRacionesRestantes(totalRacionesPreparadas); // Inicializa stock restante
            nuevaPreparacion.setEstadoPreparacion(EstadoPreparacion.FINALIZADA);
            nuevaPreparacion.setActiva(true);

            return preparacionRepository.save(nuevaPreparacion);

        } catch (RuntimeException e) {
            if (e instanceof FechaInvalidaException || e instanceof EstadoInvalidoException ||
                e instanceof PreparacionDuplicadaException || e instanceof StockInsuficienteException) {
                throw e;
            }
            throw new RuntimeException("Error al registrar la preparación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void darBajaPreparacion(Long id) { 
        try {
            Preparacion preparacion = preparacionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Preparación no encontrada con id: " + id));

            if (!preparacion.isActiva()) {
                throw new IllegalArgumentException("La preparación con ID " + id + " ya se encuentra dada de baja.");
            }

            // REINTEGRO DE STOCK DE INGREDIENTES
            Receta recetaAsociada = preparacion.getReceta();
            Integer racionesPreparadasOriginales = preparacion.getTotalRacionesPreparadas(); 

            for (ItemReceta item : recetaAsociada.getItems()) { 
                if (!item.isActivo()) continue;

                if (!(item.getIngrediente() instanceof Producto)) {
                    continue; 
                }

                Producto producto = (Producto) item.getIngrediente(); // Castea a Producto
                Double cantidadAIncrementar = item.getCantidad() * racionesPreparadasOriginales;
                ingredienteService.agregarStockProducto(producto.getId(), cantidadAIncrementar); 
            }

            preparacion.setActiva(false);
            preparacionRepository.save(preparacion);

        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("Error al dar de baja la preparación: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Preparacion modificarDatosDePreparacion(LocalDate nuevaFechaCoccion, Long idPreparacion) { 
        try {
            Preparacion preparacion = preparacionRepository.findById(idPreparacion)
                    .orElseThrow(() -> new RuntimeException("Preparación no encontrada con id: " + idPreparacion));

            if (nuevaFechaCoccion.isAfter(LocalDate.now())) { 
                throw new FechaInvalidaException("La nueva fecha de preparación no puede ser futura.");
            }
            
            if (!preparacion.getFechaCoccion().equals(nuevaFechaCoccion)) { 
                Optional<Preparacion> preparacionExistente = preparacionRepository.findByReceta_IdAndFechaCoccion(
                    preparacion.getReceta().getId(), nuevaFechaCoccion);
                if (preparacionExistente.isPresent() && !preparacionExistente.get().getId().equals(idPreparacion)) {
                    throw new PreparacionDuplicadaException("Ya existe otra preparación con la misma receta para la nueva fecha.");
                }
            }

            preparacion.setFechaCoccion(nuevaFechaCoccion); 
            return preparacionRepository.save(preparacion);

        } catch (RuntimeException e) {
             if (e instanceof FechaInvalidaException || e instanceof PreparacionDuplicadaException) {
                throw e;
            }
            throw new RuntimeException("Error al modificar la fecha de la preparación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PreparacionListadoDTO> listarPreparacionesActivas(LocalDate fechaFiltrar, String nombreRecetaFiltrar) {
        try {
            List<Preparacion> preparaciones;

            if (fechaFiltrar != null && nombreRecetaFiltrar != null && !nombreRecetaFiltrar.trim().isEmpty()) {
                preparaciones = preparacionRepository.findByFechaCoccionAndRecetaNombreContainingIgnoreCaseAndActivaTrue(fechaFiltrar, nombreRecetaFiltrar);
            } else if (fechaFiltrar != null) {
                preparaciones = preparacionRepository.findByFechaCoccionAndActivaTrue(fechaFiltrar); 
            } else if (nombreRecetaFiltrar != null && !nombreRecetaFiltrar.trim().isEmpty()) {
                preparaciones = preparacionRepository.findByRecetaNombreContainingIgnoreCaseAndActivaTrue(nombreRecetaFiltrar);
            } else {
                preparaciones = preparacionRepository.findByActivaTrue();
            }

            return preparaciones.stream()
                    .map(p -> {
                        int caloriasPorPlato = p.getReceta().getCaloriasTotales(); 
                        return new PreparacionListadoDTO( 
                                p.getReceta().getNombre(),
                                p.getFechaCoccion(), 
                                p.getTotalRacionesPreparadas(), 
                                caloriasPorPlato
                        );
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar preparaciones activas: " + e.getMessage(), e);
        }
    }
}