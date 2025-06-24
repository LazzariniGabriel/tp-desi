package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.RecetaActivaDTO;
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.repositorys.PreparacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PreparacionServiceImpl implements PreparacionServiceInterface{

    @Autowired
    private PreparacionRepository preparacionRepository;


    @Override
    public Preparacion registrarPreparacion(LocalDate fechaValida, Long idReceta, Integer cantidadRaciones) {
        return null;
    }

    @Override
    public void darBajaPreparacion(Long idPreparacion) {

    }

    @Override
    public Preparacion modificarDatosDePreparacion(LocalDate nuevaFecha, Long idPreparacion) {
        return null;
    }

    @Override
    public List<RecetaActivaDTO> listarRecetasActivas(LocalDate fechaFiltrar, String nombreFiltrar) {
        return null;
    }
}
