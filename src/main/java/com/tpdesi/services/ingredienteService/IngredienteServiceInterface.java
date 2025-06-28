package com.tpdesi.services.ingredienteService;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.entitys.types.Condimento;
import com.tpdesi.entitys.types.Producto;

import java.util.List;

public interface IngredienteServiceInterface {

    Producto crearProducto(Producto producto);
    Condimento crearCondimento(Condimento condimento);

    List<Ingrediente> listarTodosLosIngredientes();
    List<Producto> listarProductos();
    List<Condimento> listarCondimentos();

    Ingrediente obtenerIngredientePorId(Long id);

    void eliminarIngredienteLogicamente(Long id);

    Producto actualizarStockProducto(Long idProducto, Double nuevoStock);
    Producto agregarStockProducto(Long idProducto, Double cantidadAAgregar);
    Producto descontarStockProducto(Long idProducto, Double cantidadADescontar);

    // Obtener el stock de un Producto por su ID
    Double obtenerStockProducto(Long idProducto); 
}