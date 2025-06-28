package com.tpdesi.services;

import com.tpdesi.entitys.EntregaAsistencia; 
import com.tpdesi.entitys.Familia;
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.exceptionHandler.StockInsuficienteException;
import com.tpdesi.repositorys.EntregaAsistenciaRepository; 
import com.tpdesi.repositorys.FamiliaRepository;
import com.tpdesi.repositorys.PreparacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaService {

    @Autowired
    private EntregaAsistenciaRepository entregaAsistenciaRepository; 

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private PreparacionRepository preparacionRepository;

    @Transactional
    public EntregaAsistencia registrarEntrega(Long familiaId, Long preparacionId, int cantidadRaciones) { 
        Familia familia = familiaRepository.findById(familiaId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con ID: " + familiaId));

        Preparacion preparacion = preparacionRepository.findById(preparacionId)
                .orElseThrow(() -> new RuntimeException("Preparación no encontrada con ID: " + preparacionId));

        if (!preparacion.isActiva()) {
            throw new IllegalArgumentException("La preparación seleccionada no está activa o ha sido dada de baja.");
        }

        LocalDate hoy = LocalDate.now();

        if (entregaAsistenciaRepository.findByFamiliaAndFecha(familia, hoy).isPresent()) { 
            throw new IllegalArgumentException("Ya se realizó una entrega hoy para esta familia.");
        }

        long integrantesActivos = familia.getCantidadIntegrantesActivos();
        if (cantidadRaciones > integrantesActivos) { 
            throw new IllegalArgumentException("No se pueden entregar más raciones (" + cantidadRaciones + ") que integrantes activos (" + integrantesActivos + ") en la familia.");
        }

        if (preparacion.getStockRacionesRestantes() == null || preparacion.getStockRacionesRestantes() < cantidadRaciones) { 
            throw new StockInsuficienteException(
                "Stock de raciones insuficiente para la preparación. Disponibles: " +
                (preparacion.getStockRacionesRestantes() != null ? preparacion.getStockRacionesRestantes() : 0) +
                ", Requeridas: " + cantidadRaciones
            );
        }
        preparacion.setStockRacionesRestantes(preparacion.getStockRacionesRestantes() - cantidadRaciones); 
        preparacionRepository.save(preparacion);

        EntregaAsistencia entrega = new EntregaAsistencia(); 
        entrega.setFamilia(familia);
        entrega.setPreparacion(preparacion);
        entrega.setCantidadRaciones(cantidadRaciones); 
        entrega.setFecha(hoy); 
        entrega.setActivo(true);

        return entregaAsistenciaRepository.save(entrega);
    }

    @Transactional
    public void eliminarEntrega(Long id) {
        EntregaAsistencia entrega = entregaAsistenciaRepository.findById(id) 
                .orElseThrow(() -> new RuntimeException("Entrega de asistencia no encontrada con ID: " + id));

        if (!entrega.isActivo()) {
            throw new IllegalArgumentException("La entrega de asistencia con ID " + id + " ya se encuentra dada de baja.");
        }

        Preparacion preparacion = entrega.getPreparacion();
        if (preparacion != null) {
            int racionesDevueltas = entrega.getCantidadRaciones();
            preparacion.setStockRacionesRestantes( 
                (preparacion.getStockRacionesRestantes() != null ? preparacion.getStockRacionesRestantes() : 0) + racionesDevueltas
            );
            preparacionRepository.save(preparacion);
        } else {
            System.err.println("Advertencia: Entrega de asistencia " + id + " no tiene preparación asociada para reintegrar stock.");
        }

        entrega.setActivo(false);
        entregaAsistenciaRepository.save(entrega); 
    }

    public List<EntregaAsistencia> listarPorFecha(LocalDate fecha) { 
        return entregaAsistenciaRepository.findByFechaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreContainingIgnoreCase( 
            fecha, "", ""
        ).stream().filter(EntregaAsistencia::isActivo).toList();
    }

    public List<EntregaAsistencia> buscarPorFiltros(LocalDate fecha, String nroFamilia, String nombreFamilia) { 
        String finalNroFamilia = (nroFamilia == null || nroFamilia.trim().isEmpty()) ? "" : nroFamilia;
        String finalNombreFamilia = (nombreFamilia == null || nombreFamilia.trim().isEmpty()) ? "" : nombreFamilia;

        return entregaAsistenciaRepository.findByFechaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreContainingIgnoreCase( 
                fecha,
                finalNroFamilia,
                finalNombreFamilia
        ).stream().filter(EntregaAsistencia::isActivo).toList();
    }
}