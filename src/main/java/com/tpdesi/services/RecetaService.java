package com.tpdesi.services;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.entitys.ItemReceta; 
import com.tpdesi.entitys.Receta;
import com.tpdesi.repositorys.IngredienteRepository;
import com.tpdesi.repositorys.ItemRecetaRepository; 
import com.tpdesi.repositorys.RecetaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository; 

    @Autowired
    private ItemRecetaRepository itemRecetaRepository; 

    @Transactional
    public Receta altaReceta(Receta receta) {
        if (recetaRepository.findByNombreIgnoreCase(receta.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una receta con el nombre: " + receta.getNombre());
        }

        for (ItemReceta item : receta.getItems()) { 
            Ingrediente ingredienteCatalogo;
            if (item.getIngrediente().getId() != null) {
                ingredienteCatalogo = ingredienteRepository.findById(item.getIngrediente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ingrediente del catálogo no encontrado con ID: " + item.getIngrediente().getId()));
            } else {
                ingredienteCatalogo = ingredienteRepository.findByNombreIgnoreCase(item.getIngrediente().getNombre())
                    .orElseThrow(() -> new IllegalArgumentException("Ingrediente del catálogo no encontrado con nombre: " + item.getIngrediente().getNombre() + ". Debe seleccionar un ingrediente existente."));
            }
            item.setIngrediente(ingredienteCatalogo);
            item.setReceta(receta);
            item.setActivo(true);
        }

        receta.setActiva(true);
        return recetaRepository.save(receta);
    }

    @Transactional
    public Receta bajaReceta(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
        receta.setActiva(false);
        return recetaRepository.save(receta);
    }

    @Transactional
    public Receta modificarReceta(Long id, Receta recetaActualizada) {
        Receta recetaExistente = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));

        recetaExistente.setDescripcion(recetaActualizada.getDescripcion()); 

        recetaExistente.getItems().forEach(itemRecetaExistente -> { 
            boolean found = recetaActualizada.getItems().stream() 
                    .anyMatch(irNuevo -> Objects.equals(irNuevo.getId(), itemRecetaExistente.getId()));
            if (!found) {
                itemRecetaExistente.setActivo(false);
            }
        });

        for (ItemReceta itemNuevo : recetaActualizada.getItems()) { 
            if (itemNuevo.getId() == null) {
                Ingrediente ingredienteCatalogo = ingredienteRepository.findById(itemNuevo.getIngrediente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Ingrediente del catálogo no encontrado con ID: " + itemNuevo.getIngrediente().getId()));
                itemNuevo.setIngrediente(ingredienteCatalogo);
                recetaExistente.addItem(itemNuevo); 
            } else {
                recetaExistente.getItems().stream() 
                        .filter(itemExistente -> Objects.equals(itemExistente.getId(), itemNuevo.getId()))
                        .findFirst()
                        .ifPresent(itemExistente -> {
                            if (!Objects.equals(itemExistente.getIngrediente().getId(), itemNuevo.getIngrediente().getId())) {
                                Ingrediente nuevoIngredienteCatalogo = ingredienteRepository.findById(itemNuevo.getIngrediente().getId())
                                    .orElseThrow(() -> new IllegalArgumentException("Nuevo ingrediente del catálogo no encontrado con ID: " + itemNuevo.getIngrediente().getId()));
                                itemExistente.setIngrediente(nuevoIngredienteCatalogo);
                            }
                            itemExistente.setCantidad(itemNuevo.getCantidad());
                            itemExistente.setActivo(itemNuevo.isActivo());
                        });
            }
        }
        return recetaRepository.save(recetaExistente);
    }

    public List<Receta> listarRecetasActivas() {
        return recetaRepository.findByActivaTrue();
    }

    public Receta obtenerRecetaPorId(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
    }

    public List<Receta> buscarRecetas(String nombreReceta, Double caloriasMin, Double caloriasMax) {
        List<Receta> recetasActivas = recetaRepository.findByActivaTrue();

        if (nombreReceta != null && !nombreReceta.trim().isEmpty()) {
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> receta.getNombre().toLowerCase().contains(nombreReceta.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (caloriasMin != null && caloriasMax != null) {
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> {
                        int caloriasTotales = receta.getCaloriasTotales();
                        return caloriasTotales >= caloriasMin && caloriasTotales <= caloriasMax;
                    })
                    .collect(Collectors.toList());
        } else if (caloriasMin != null) {
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> receta.getCaloriasTotales() >= caloriasMin)
                    .collect(Collectors.toList());
        } else if (caloriasMax != null) {
            recetasActivas = recetasActivas.stream()
                    .filter(receta -> receta.getCaloriasTotales() <= caloriasMax)
                    .collect(Collectors.toList());
        }

        return recetasActivas;
    }

    public Receta buscarRecetaPorId(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
    }
}