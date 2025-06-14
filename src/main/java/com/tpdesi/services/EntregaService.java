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

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;
    @Autowired
    private FamiliaRepository familiaRepository;
    @Autowired
    private RecetaRepository recetaRepository;

    @Transactional
    public Entrega registrarEntrega(Long familiaId, Long recetaId, int raciones) {
        Familia familia = familiaRepository.findById(familiaId)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada"));
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        LocalDate hoy = LocalDate.now();

        if (entregaRepository.findByFamiliaAndFechaEntrega(familia, hoy).isPresent()) {
            throw new IllegalArgumentException("Ya se realizó una entrega hoy para esta familia.");
        }

        long integrantesActivos = familia.getCantidadIntegrantesActivos();
        if (raciones > integrantesActivos) {
            throw new IllegalArgumentException("No se pueden entregar más raciones que integrantes activos.");
        }

        // Descontar stock (lógica interna pendiente)
        // ...

        Entrega entrega = new Entrega();
        entrega.setFamilia(familia);
        entrega.setReceta(receta);
        entrega.setCantidadRaciones(raciones);
        entrega.setFechaEntrega(hoy);
        entrega.setActivo(true);

        return entregaRepository.save(entrega);
    }

    @Transactional
    public void eliminarEntrega(Long id) {
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
        entrega.setActivo(false);

        // Restaurar stock (lógica pendiente)
        // ...

        entregaRepository.save(entrega);
    }

    public List<Entrega> listarPorFecha(LocalDate fecha) {
        return entregaRepository.findByFechaEntrega(fecha);
    }

    public List<Entrega> buscarPorFiltros(LocalDate fecha, String nroFamilia, String nombreFamilia) {
        return entregaRepository.findByFechaEntregaAndFamilia_NroFamiliaContainingIgnoreCaseAndFamilia_NombreFamiliaContainingIgnoreCase(
                fecha, nroFamilia == null ? "" : nroFamilia,
                nombreFamilia == null ? "" : nombreFamilia
        );
    }
}
