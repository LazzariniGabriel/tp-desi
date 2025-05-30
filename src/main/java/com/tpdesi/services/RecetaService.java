package com.tpdesi.services;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.entitys.IngredienteReceta;
import com.tpdesi.entitys.Receta;
import com.tpdesi.repositorys.IngredienteRepository;
import com.tpdesi.repositorys.RecetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository; // Para el catálogo de ingredientes

    // --- ALTA DE RECETA ---
    @Transactional
    public Receta altaReceta(Receta receta) {
        // Criterio: No podrá haber dos recetas con el mismo nombre.
        if (recetaRepository.findByNombreIgnoreCase(receta.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una receta con el nombre: " + receta.getNombre());
        }

        // Procesar ingredientes de la receta
        for (IngredienteReceta ir : receta.getIngredientes()) {
            // Criterio: Ingrediente (lista de selección de los ingredientes dados de alta en la base de datos)
            // Asegurar que el ingrediente del catálogo existe o crearlo si es necesario (según el enunciado, debería venir de una lista de selección)
            // Aquí asumimos que si viene un ingrediente con ID, ya existe. Si no, lo buscamos por nombre o lanzamos error.
            Ingrediente ingredienteCatalogo;
            if (ir.getIngrediente().getId() != null) {
                ingredienteCatalogo = ingredienteRepository.findById(ir.getIngrediente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ingrediente del catálogo no encontrado con ID: " + ir.getIngrediente().getId()));
            } else { // Si no viene ID, asumimos que viene el nombre y lo buscamos
                ingredienteCatalogo = ingredienteRepository.findByNombreIgnoreCase(ir.getIngrediente().getNombre())
                    .orElseThrow(() -> new IllegalArgumentException("Ingrediente del catálogo no encontrado con nombre: " + ir.getIngrediente().getNombre() + ". Debe seleccionar un ingrediente existente."));
            }
            ir.setIngrediente(ingredienteCatalogo); // Asignar el ingrediente del catálogo persistido
            ir.setReceta(receta); // Establecer la relación bidireccional
            ir.setActivo(true); // Asegurar que el ingrediente de la receta está activo al dar de alta
        }

        receta.setActiva(true); // Una receta recién creada está activa
        return recetaRepository.save(receta);
    }

    // --- BAJA DE RECETA (LÓGICA) ---
    @Transactional
    public Receta bajaReceta(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
        receta.setActiva(false); // Eliminación lógica
        return recetaRepository.save(receta);
    }

    // --- MODIFICAR RECETA ---
    @Transactional
    public Receta modificarReceta(Long id, Receta recetaActualizada) {
        Receta recetaExistente = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));

        // Criterio: Se podrán editar todos los datos excepto el nombre (solo lectura)
        recetaExistente.setDescripcionPreparacion(recetaActualizada.getDescripcionPreparacion());
        // El nombre no se actualiza
        // El estado 'activa' de la receta no se actualiza por PUT, solo por DELETE lógico.

        // Criterio: La eliminación de ingredientes de la receta será eliminación lógica
        // Identificar y dar de baja lógicamente los IngredienteReceta que ya no están en la lista
        recetaExistente.getIngredientes().forEach(ingredienteRecetaExistente -> {
            boolean found = recetaActualizada.getIngredientes().stream()
                    .anyMatch(irNuevo -> Objects.equals(irNuevo.getId(), ingredienteRecetaExistente.getId()));
            if (!found) {
                ingredienteRecetaExistente.setActivo(false); // Baja lógica del ingrediente en la receta
            }
        });

        // Actualizar o agregar nuevos IngredienteReceta
        for (IngredienteReceta irNuevo : recetaActualizada.getIngredientes()) {
            if (irNuevo.getId() == null) { // Nuevo ingrediente a la receta
                // Asegurarse de que el Ingrediente del catálogo existe
                Ingrediente ingredienteCatalogo = ingredienteRepository.findById(irNuevo.getIngrediente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ingrediente del catálogo no encontrado con ID: " + irNuevo.getIngrediente().getId()));
                irNuevo.setIngrediente(ingredienteCatalogo);
                recetaExistente.addIngrediente(irNuevo); // addIngrediente se encarga de setear la relación bidireccional y agregar a la lista
            } else { // Ingrediente existente en la receta, actualizar
                recetaExistente.getIngredientes().stream()
                        .filter(irExistente -> Objects.equals(irExistente.getId(), irNuevo.getId()))
                        .findFirst()
                        .ifPresent(irExistente -> {
                            // Criterio: Ingrediente (lista de selección de los ingredientes dados de alta en la base de datos)
                            // Si se cambia el ingrediente asociado (del catálogo)
                            if (!Objects.equals(irExistente.getIngrediente().getId(), irNuevo.getIngrediente().getId())) {
                                Ingrediente nuevoIngredienteCatalogo = ingredienteRepository.findById(irNuevo.getIngrediente().getId())
                                    .orElseThrow(() -> new IllegalArgumentException("Nuevo ingrediente del catálogo no encontrado con ID: " + irNuevo.getIngrediente().getId()));
                                irExistente.setIngrediente(nuevoIngredienteCatalogo);
                            }
                            irExistente.setCantidad(irNuevo.getCantidad());
                            irExistente.setCalorias(irNuevo.getCalorias());
                            irExistente.setActivo(irNuevo.isActivo()); // Permitir reactivar/desactivar individualmente un ingrediente de la receta
                        });
            }
        }
        return recetaRepository.save(recetaExistente);
    }

    // --- LISTAR RECETAS ---
    public List<Receta> listarRecetasActivas() {
        return recetaRepository.findByActivaTrue();
    }

    public Receta obtenerRecetaPorId(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
    }

    public List<Receta> buscarRecetas(String nombreReceta, Double caloriasMin, Double caloriasMax) {
        List<Receta> recetasActivas = recetaRepository.findByActivaTrue();

        // Filtrar por nombre si se proporciona
        if (nombreReceta != null && !nombreReceta.trim().isEmpty()) {
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> receta.getNombre().toLowerCase().contains(nombreReceta.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtrar por rango de calorías si se proporciona
        if (caloriasMin != null && caloriasMax != null) {
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> {
                        int caloriasTotales = receta.getCaloriasTotales();
                        return caloriasTotales >= caloriasMin && caloriasTotales <= caloriasMax;
                    })
                    .collect(Collectors.toList());
        } else if (caloriasMin != null) { // Si solo se proporciona un mínimo
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> receta.getCaloriasTotales() >= caloriasMin)
                    .collect(Collectors.toList());
        } else if (caloriasMax != null) { // Si solo se proporciona un máximo
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> receta.getCaloriasTotales() <= caloriasMax)
                    .collect(Collectors.toList());
        }

        return recetasActivas;
    }
}