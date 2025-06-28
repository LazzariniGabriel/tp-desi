package com.tpdesi.services;

import com.tpdesi.entitys.Entrega;
import com.tpdesi.entitys.Familia;
import com.tpdesi.entitys.Preparacion; // Importar Preparacion
// import com.tpdesi.entitys.Receta; // Ya no se usará Receta directamente en Entrega
import com.tpdesi.exceptionHandler.StockInsuficienteException; // Importar tu excepción de Stock
import com.tpdesi.repositorys.EntregaRepository;
import com.tpdesi.repositorys.FamiliaRepository;
import com.tpdesi.repositorys.PreparacionRepository; // Importar PreparacionRepository
// import com.tpdesi.repositorys.RecetaRepository; // Ya no se usará RecetaRepository directamente en Entrega
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private PreparacionRepository preparacionRepository; // INYECTADO: Para acceder a las Preparaciones

    /**
     * Método principal para registrar una nueva entrega de alimentos.
     * Se asegura de cumplir con las validaciones requeridas antes de guardar.
     */
    @Transactional
    public Entrega registrarEntrega(Long familiaId, Long preparacionId, int raciones) { // CAMBIO: de recetaId a preparacionId
        // Buscamos la familia y preparación asociadas por su ID (si no se encuentran, lanza error)
        Familia familia = familiaRepository.findById(familiaId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con ID: " + familiaId));

        Preparacion preparacion = preparacionRepository.findById(preparacionId) // CAMBIO: Buscar Preparacion
                .orElseThrow(() -> new RuntimeException("Preparación no encontrada con ID: " + preparacionId));

        // Validar que la preparación esté activa
        if (!preparacion.isActiva()) { // Asumiendo que Preparacion tiene un campo 'activa'
            throw new IllegalArgumentException("La preparación seleccionada no está activa o ha sido dada de baja.");
        }

        LocalDate hoy = LocalDate.now();

        // Validamos que no se haya registrado ya una entrega para esta familia en el día
        if (entregaRepository.findByFamiliaAndFechaEntrega(familia, hoy).isPresent()) {
            throw new IllegalArgumentException("Ya se realizó una entrega hoy para esta familia.");
        }

        // Verificamos que la cantidad de raciones no supere los integrantes activos de la familia
        long integrantesActivos = familia.getCantidadIntegrantesActivos();
        if (raciones > integrantesActivos) {
            throw new IllegalArgumentException("No se pueden entregar más raciones (" + raciones + ") que integrantes activos (" + integrantesActivos + ") en la familia.");
        }

        // LÓGICA CRÍTICA: Descontar las raciones del stock de la preparación
        if (preparacion.getRacionesDisponibles() == null || preparacion.getRacionesDisponibles() < raciones) {
            throw new StockInsuficienteException(
                "Stock de raciones insuficiente para la preparación. Disponibles: " + 
                (preparacion.getRacionesDisponibles() != null ? preparacion.getRacionesDisponibles() : 0) + 
                ", Requeridas: " + raciones
            );
        }
        preparacion.setRacionesDisponibles(preparacion.getRacionesDisponibles() - raciones);
        preparacionRepository.save(preparacion); // Guardar la preparación con el stock actualizado

        // Creamos el objeto Entrega con los datos recibidos y validados
        Entrega entrega = new Entrega();
        entrega.setFamilia(familia);
        entrega.setPreparacion(preparacion); // CAMBIO: Asociar a Preparacion
        entrega.setCantidadRaciones(raciones);
        entrega.setFechaEntrega(hoy);
        entrega.setActivo(true);

        return entregaRepository.save(entrega);
    }

    /**
     * Método para eliminar una entrega registrada.
     * Marca la entrega como inactiva y DEVUELVE el stock a la preparación.
     */
    @Transactional
    public void eliminarEntrega(Long id) {
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada con ID: " + id));

        if (!entrega.isActivo()) { // Evitar reintegrar si ya estaba de baja
            throw new IllegalArgumentException("La entrega con ID " + id + " ya se encuentra dada de baja.");
        }

        // LÓGICA CRÍTICA: Devolver al stock las raciones entregadas a la Preparacion
        Preparacion preparacion = entrega.getPreparacion(); // Obtener la Preparacion asociada
        if (preparacion != null) {
            int racionesDevueltas = entrega.getCantidadRaciones();
            preparacion.setRacionesDisponibles(
                (preparacion.getRacionesDisponibles() != null ? preparacion.getRacionesDisponibles() : 0) + racionesDevueltas
            );
            preparacionRepository.save(preparacion); // Guardar la preparación con el stock reintegrado
        } else {
            // Esto no debería pasar si la relación es obligatoria (nullable = false)
            System.err.println("Advertencia: Entrega " + id + " no tiene preparación asociada para reintegrar stock.");
        }

        // Marcar la entrega como inactiva (eliminación lógica)
        entrega.setActivo(false);
        entregaRepository.save(entrega);
    }

    /**
     * Devuelve todas las entregas realizadas en una fecha específica (solo activas).
     */
    public List<Entrega> listarPorFecha(LocalDate fecha) {
        // Asumiendo que el listado solo quiere las entregas activas
        return entregaRepository.findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
            fecha, "", "" // Pasar cadenas vacías para que los filtros por nombre no restrinjan
        ).stream().filter(Entrega::isActivo).toList(); // Asegurar que solo se devuelven las activas
    }

    /**
     * Permite buscar entregas por fecha y también por número y nombre de familia (filtros opcionales).
     * Siempre devuelve solo entregas activas.
     */
    public List<Entrega> buscarPorFiltros(LocalDate fecha, String nroFamilia, String nombreFamilia) {
        // Si los filtros son nulos, los reemplazamos por strings vacíos para que no interfieran en la búsqueda
        String finalNroFamilia = (nroFamilia == null || nroFamilia.trim().isEmpty()) ? "" : nroFamilia;
        String finalNombreFamilia = (nombreFamilia == null || nombreFamilia.trim().isEmpty()) ? "" : nombreFamilia;

        return entregaRepository.findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
                fecha,
                finalNroFamilia,
                finalNombreFamilia
        ).stream().filter(Entrega::isActivo).toList(); // Asegurar que solo se devuelven las activas
    }
}