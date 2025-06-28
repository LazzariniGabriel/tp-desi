package com.tpdesi.services.ingredienteService;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.entitys.types.Condimento;
import com.tpdesi.entitys.types.Producto;
import com.tpdesi.exceptionHandler.StockInsuficienteException;
import com.tpdesi.repositorys.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredienteServiceImpl implements IngredienteServiceInterface {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        if (ingredienteRepository.findByNombreIgnoreCase(producto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un ingrediente (producto o condimento) con el nombre: " + producto.getNombre());
        }
        producto.setActivo(true);
        producto.setStockDisponible(producto.getStockDisponible() != null ? producto.getStockDisponible() : 0.0);
        return ingredienteRepository.save(producto);
    }

    @Override
    @Transactional
    public Condimento crearCondimento(Condimento condimento) {
        if (ingredienteRepository.findByNombreIgnoreCase(condimento.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un ingrediente (producto o condimento) con el nombre: " + condimento.getNombre());
        }
        condimento.setActivo(true);
        return ingredienteRepository.save(condimento);
    }

    @Override
    public List<Ingrediente> listarTodosLosIngredientes() {
        return ingredienteRepository.findAll();
    }

    @Override
    public List<Producto> listarProductos() {
        return ingredienteRepository.findProductos();
    }

    @Override
    public List<Condimento> listarCondimentos() {
        return ingredienteRepository.findCondimentos();
    }

    @Override
    public Ingrediente obtenerIngredientePorId(Long id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void eliminarIngredienteLogicamente(Long id) {
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con ID: " + id));
        ingrediente.setActivo(false);
        ingredienteRepository.save(ingrediente);
    }

    @Override
    @Transactional
    public Producto actualizarStockProducto(Long idProducto, Double nuevoStock) {
        Producto producto = ingredienteRepository.findProductoById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o no es un Producto con ID: " + idProducto));
        
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        producto.setStockDisponible(nuevoStock);
        return ingredienteRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto agregarStockProducto(Long idProducto, Double cantidadAAgregar) {
        Producto producto = ingredienteRepository.findProductoById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o no es un Producto con ID: " + idProducto));

        if (cantidadAAgregar <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor que cero.");
        }
        producto.setStockDisponible((producto.getStockDisponible() != null ? producto.getStockDisponible() : 0.0) + cantidadAAgregar);
        return ingredienteRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto descontarStockProducto(Long idProducto, Double cantidadADescontar) {
        Producto producto = ingredienteRepository.findProductoById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o no es un Producto con ID: " + idProducto));

        if (cantidadADescontar <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor que cero.");
        }

        Double stockActual = producto.getStockDisponible() != null ? producto.getStockDisponible() : 0.0;
        if (stockActual < cantidadADescontar) {
            throw new StockInsuficienteException("Stock insuficiente para " + producto.getNombre() + ". Stock actual: " + stockActual + ", requerido: " + cantidadADescontar);
        }

        producto.setStockDisponible(stockActual - cantidadADescontar);
        return ingredienteRepository.save(producto);
    }

    // Implementación para obtener el stock de un Producto
    @Override
    public Double obtenerStockProducto(Long idProducto) {
        Producto producto = ingredienteRepository.findProductoById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o no es un Producto con ID: " + idProducto));
        return producto.getStockDisponible() != null ? producto.getStockDisponible() : 0.0;
    }
}