package com.tpdesi.repositorys;

import com.tpdesi.entitys.Ingrediente;
import com.tpdesi.entitys.types.Condimento;
import com.tpdesi.entitys.types.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    Optional<Ingrediente> findByNombreIgnoreCase(String nombre);

    
    List<Producto> findAllProductos(); 
    List<Condimento> findAllCondimentos();

  
    @Query("SELECT i FROM Ingrediente i WHERE TYPE(i) = Producto")
    List<Producto> findProductos();

    @Query("SELECT i FROM Ingrediente i WHERE TYPE(i) = Condimento")
    List<Condimento> findCondimentos();

   
    @Query("SELECT i FROM Ingrediente i WHERE i.id = :id AND TYPE(i) = Producto")
    Optional<Producto> findProductoById(Long id);
}