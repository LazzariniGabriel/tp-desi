package com.tpdesi.controllers;

import com.tpdesi.entitys.Familia;
import com.tpdesi.services.FamiliaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familias")
public class FamiliaController {

    private final FamiliaService familiaService;

    public FamiliaController(FamiliaService familiaService) {
        this.familiaService = familiaService;
    }

    @PostMapping
    public Familia crearFamilia(@RequestBody Familia familia) {
        return familiaService.crearFamilia(familia);
    }

    @GetMapping
    public List<Familia> listar() {
        return familiaService.listarActivas();
    }

    @PutMapping("/{id}")
    public Familia modificar(@PathVariable Long id, @RequestBody Familia familia) {
        familia.setId(id);
        return familiaService.modificarFamilia(familia);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        familiaService.eliminarLogicamente(id);
    }

    @GetMapping("/{id}")
    public Familia obtenerPorId(@PathVariable Long id) {
        return familiaService.obtenerPorId(id);
    }
}
