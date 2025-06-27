package com.tpdesi.services.ingredienteService;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.repositorys.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredienteServiceImpl implements IngredienteServiceInterface {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Override
    public Ingrediente crearIngrediente(Ingrediente ingrediente) {
        try {
            if (ingredienteRepository.findByNombreIgnoreCase(ingrediente.getNombre()).isPresent()) {
                throw new RuntimeException("Ya existe un ingrediente con el nombre: " + ingrediente.getNombre());
            }
            ingrediente.setActivo(true);
            return ingredienteRepository.save(ingrediente);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el ingrediente: " + e.getMessage());
        }
    }

    @Override
    public List<Ingrediente> listarIngredientes() {
        try {
            return ingredienteRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar ingredientes: " + e.getMessage());
        }
    }

    @Override
    public Ingrediente actualizarStock(Long id, Double nuevaCantidad) {
        try {
            Ingrediente ingrediente = ingredienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con id " + id));
            ingrediente.setCantidadEnStock(nuevaCantidad);
            return ingredienteRepository.save(ingrediente);
        } catch (Exception e) {
            throw new RuntimeException("error al actualizar el stock: " + e.getMessage());
        }
    }

    @Override
    public void eliminarIngrediente(Long id) {
        try {
            Ingrediente ingrediente = ingredienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con ID: " + id));
            ingrediente.setActivo(false);
            ingredienteRepository.save(ingrediente);
        } catch (Exception e) {
            throw new RuntimeException("error al eliminar el ingrediente: " + e.getMessage());
        }
    }

    @Override
    public Ingrediente agregarStock(Long idIngrediente, Double pesoEnKG) {
        try {
            Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente)
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con id: " + idIngrediente));

            if (pesoEnKG <= 0) {
                throw new RuntimeException("La cantidad a agregar debe ser mayor que cero.");
            }

            Double nuevoStock = (ingrediente.getCantidadEnStock() != null ? ingrediente.getCantidadEnStock() : 0.0) + pesoEnKG;
            ingrediente.setCantidadEnStock(nuevoStock);
            return ingredienteRepository.save(ingrediente);
        } catch (Exception e) {
            throw new RuntimeException("error al agregar stock: " + e.getMessage());
        }
    }

    @Override
    public Ingrediente decrementarStock(Long idIngrediente, Double pesoStockADescontar) {
        try {
            Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente)
                    .orElseThrow(() -> new RuntimeException("ingrediente no encontrado con id: " + idIngrediente));

            if (pesoStockADescontar <= 0) {
                throw new RuntimeException("La cantidad a descontar debe ser mayor que cero.");
            }

            Double stockActual = ingrediente.getCantidadEnStock() != null ? ingrediente.getCantidadEnStock() : 0.0;
            if (stockActual - pesoStockADescontar < 0) {
                throw new RuntimeException("Stock insuficiente. Stock actual: " + stockActual + ", requerido: " + pesoStockADescontar);
            }

            ingrediente.setCantidadEnStock(stockActual - pesoStockADescontar);
            return ingredienteRepository.save(ingrediente);
        } catch (Exception e) {
            throw new RuntimeException("Error al descontar stock: " + e.getMessage());
        }
    }

    @Override
    public boolean verificarDisponibilidadStock(Long idIngrediente, Double pesoAUsar) {
        try {
            Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente)
                    .orElseThrow(() -> new RuntimeException("ingrediente no encontrado con id: " + idIngrediente));

            Double stock = ingrediente.getCantidadEnStock() != null ? ingrediente.getCantidadEnStock() : 0.0;
            return stock - pesoAUsar >= 0;
        } catch (Exception e) {
            throw new RuntimeException("error al verificar stock: " + e.getMessage());
        }
    }


    @Override
    public Double obtenerStock(Long idIngrediente) {
        try {
            Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente)
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con id: " + idIngrediente));
            return ingrediente.getCantidadEnStock() != null ? ingrediente.getCantidadEnStock() : 0.0;
        } catch (Exception e) {
            throw new RuntimeException("error al obtener el stock del ingrediente: " + e.getMessage());
        }
    }

}
