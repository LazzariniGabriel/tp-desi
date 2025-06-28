package com.tpdesi.services.ingredienteService;

import com.tpdesi.entitys.Ingrediente;

import java.util.List;

public interface IngredienteServiceInterface {

    Ingrediente crearIngrediente(Ingrediente ingrediente);

    List<Ingrediente> listarIngredientes();

    Ingrediente actualizarStock(Long id, Double nuevaCantidad);

    void eliminarIngrediente(Long id);

    Ingrediente agregarStock(Long idIngrediente, Double pesoEnKG);

    Ingrediente decrementarStock(Long idIngrediente, Double pesoStockADescontar);

    boolean verificarDisponibilidadStock(Long idIngrediente, Double pesoAUsar);

    Double obtenerStock(Long idIngrediente);

}
