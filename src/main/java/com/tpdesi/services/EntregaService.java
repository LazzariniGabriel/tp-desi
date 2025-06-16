package com.tpdesi.services;

import com.tpdesi.entitys.Entrega;
import com.tpdesi.entitys.Familia;
import com.tpdesi.entitys.Receta;
import com.tpdesi.repositorys.EntregaRepository;
import com.tpdesi.repositorys.FamiliaRepository;
import com.tpdesi.repositorys.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Esta clase contiene la lógica de negocio relacionada con las entregas de alimentos
@Service
public class EntregaService {

    // Inyectamos los repositorios para acceder a la base de datos
    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    /**
     * Método principal para registrar una nueva entrega de alimentos.
     * Se asegura de cumplir con las validaciones requeridas antes de guardar.
     */
    @Transactional
    public Entrega registrarEntrega(Long familiaId, Long recetaId, int raciones) {
        // Buscamos la familia y receta asociadas por su ID (si no se encuentran, lanza error)
        Familia familia = familiaRepository.findById(familiaId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada"));

        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        LocalDate hoy = LocalDate.now(); // Obtenemos la fecha actual

        // Validamos que no se haya registrado ya una entrega para esta familia en el día
        if (entregaRepository.findByFamiliaAndFechaEntrega(familia, hoy).isPresent()) {
            throw new IllegalArgumentException("Ya se realizó una entrega hoy para esta familia.");
        }

        // Verificamos que la cantidad de raciones no supere los integrantes activos de la familia
        long integrantesActivos = familia.getCantidadIntegrantesActivos();
        if (raciones > integrantesActivos) {
            throw new IllegalArgumentException("No se pueden entregar más raciones que integrantes activos.");
        }

        // FALTA EPIC 3: ABMC de preparaciones.
        // Lógica pendiente: descontar las raciones del stock (preparación)

        // Creamos el objeto Entrega con los datos recibidos y validados
        Entrega entrega = new Entrega();
        entrega.setFamilia(familia);
        entrega.setReceta(receta);
        entrega.setCantidadRaciones(raciones);
        entrega.setFechaEntrega(hoy);
        entrega.setActivo(true); // Se marca como activa

        // Guardamos la entrega en la base de datos
        return entregaRepository.save(entrega);
    }

    /**
     * Método para eliminar una entrega registrada.
     * Marca la entrega como inactiva y debería devolver el stock (pendiente).
     */
    @Transactional
    public void eliminarEntrega(Long id) {
        // Buscamos la entrega por ID
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        // Marcamos la entrega como inactiva (eliminación lógica)
        entrega.setActivo(false);

        // FALTA EPIC 3: ABMC de preparaciones
        // Lógica pendiente: devolver al stock las raciones entregadas

        // Guardamos el cambio
        entregaRepository.save(entrega);
    }

    /**
     * Devuelve todas las entregas realizadas en una fecha específica.
     */
    public List<Entrega> listarPorFecha(LocalDate fecha) {
        return entregaRepository.findByFechaEntrega(fecha);
    }

    /**
     * Permite buscar entregas por fecha y también por número y nombre de familia (filtros opcionales).
     */
    public List<Entrega> buscarPorFiltros(LocalDate fecha, String nroFamilia, String nombreFamilia) {
        // Si los filtros son nulos, los reemplazamos por strings vacíos para que no interfieran en la búsqueda
        return entregaRepository.findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
                fecha,
                nroFamilia == null ? "" : nroFamilia,
                nombreFamilia == null ? "" : nombreFamilia
        );
    }
}
