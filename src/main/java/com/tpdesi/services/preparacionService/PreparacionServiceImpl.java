package com.tpdesi.services.preparacionService;

import com.tpdesi.DTO.RecetaActivaDTO;
import com.tpdesi.ENUM.EstadoPreparacion;
import com.tpdesi.entitys.IngredienteReceta;
import com.tpdesi.entitys.Preparacion;
import com.tpdesi.entitys.Receta;
import com.tpdesi.exceptionHandler.EstadoInvalidoException;
import com.tpdesi.exceptionHandler.FechaInvalidaException;
import com.tpdesi.exceptionHandler.PreparacionDuplicadaException;
import com.tpdesi.exceptionHandler.StockInsuficienteException;
import com.tpdesi.repositorys.PreparacionRepository;
import com.tpdesi.services.RecetaService;
import com.tpdesi.services.ingredienteService.IngredienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class PreparacionServiceImpl implements PreparacionServiceInterface {

    @Autowired
    private PreparacionRepository preparacionRepository;

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private IngredienteServiceImpl ingredienteService;




        @Override
        public Preparacion registrarPreparacion(LocalDate fechaValida, Long idReceta, Integer cantidadRaciones) {
            try {
                if (fechaValida == null || !fechaValida.isEqual(LocalDate.now())) {
                    throw new FechaInvalidaException("Fecha ingresada no es valida. La preparacion debe ser creada exactamente el dia de hoy.");
                }


                // validamos que no exista una misma preparacion con esa receta para esa fecha
                List<Preparacion> preparacionesExistentes = preparacionRepository.findByFechaPreparacion(fechaValida);
                for (Preparacion p : preparacionesExistentes) {
                    for (Receta r : p.getSeleccionDeRecetas()) {
                        if (r.getId().equals(idReceta)) {
                            throw new PreparacionDuplicadaException("Ya existe una preparacion con esta receta en la fecha indicada.");
                        }
                    }
                }

                // verificamos que la receta se encuentre activa
                Receta receta = recetaService.buscarRecetaPorId(idReceta);
                if (!receta.isActiva()) {
                    throw new EstadoInvalidoException("La receta seleccionada no esta activa.");

                }

                // aca vamos a verificar que tengamos el stock suficiente para cada ingrediente, multiplicando la cantidad
                // la cantidad de raciones de la preparacion, por lo que lleva la receta en cuanto a cada ingrediente,
                // en caso de que no sea suficiente, devolveremos la cantidad de stock que falta para tal ingrediente.
                // en caso que pase se pasara a descontar el stock de cada ingrediente.
                for (IngredienteReceta ir : receta.getIngredientes()) {
                    if (!ir.isActivo()) continue;

                    Long idIngrediente = ir.getIngrediente().getId();
                    Double cantidadTotal = ir.getCantidad() * cantidadRaciones;

                    // Obtener el stock actual del ingrediente
                    Double stockActual = ingredienteService.obtenerStock(idIngrediente);
                    if (stockActual == null) stockActual = 0.0;

                    if (stockActual < cantidadTotal) {
                        double faltante = cantidadTotal - stockActual;
                        throw new StockInsuficienteException(
                                "stock insuficiente para: " + ir.getIngrediente().getNombre() +
                                        ". Se necesitan " + String.format("%.2f", cantidadTotal) + " KG, hay " +
                                        String.format("%.2f", stockActual) + " KG. Faltan " +
                                        String.format("%.2f", faltante) + " KG."
                        );
                    }

                }


                // se descuenta del stock, la cantidad de raciones que viene en la preparacion multiplicado
                // por la cantidad que lleva de ingrediente para cada unidad de la receta (las recetas las
                // tomamos individuales)
                for (IngredienteReceta ir : receta.getIngredientes()) {
                    if (!ir.isActivo()) continue;

                    Long idIngrediente = ir.getIngrediente().getId();
                    Double cantidadTotal = ir.getCantidad() * cantidadRaciones;

                    ingredienteService.decrementarStock(idIngrediente, cantidadTotal);
                }

                // Paso las validaciones, por lo tanto instanciamos una preparacion y luego la guardamos
                // en este caso dado que es un trabajo practico y se cumple con la consigna, damos el estado
                // como finalizada. ya que el stock se desconto.
                Preparacion nueva = new Preparacion();
                nueva.setFechaPreparacion(fechaValida);
                nueva.setSeleccionDeRecetas(List.of(receta));
                nueva.setCantidadDeRacionesPreparar(cantidadRaciones);
                nueva.setEstadoPreparacion(EstadoPreparacion.FINALIZADA);
                nueva.setRecetaActiva(true);

                return preparacionRepository.save(nueva);

            } catch (Exception e) {
                throw new RuntimeException("error al registrar la preparacion: " + e.getMessage(), e);
            }
        }

    @Override
    public void darBajaPreparacion(Long idPreparacion) {
        try {
            Preparacion preparacion = preparacionRepository.findById(idPreparacion)
                    .orElseThrow(() -> new RuntimeException("Preparacion no encontrada con id: " + idPreparacion));

            preparacion.setRecetaActiva(false);
            preparacionRepository.save(preparacion);

        } catch (Exception e) {
            throw new RuntimeException("error al dar de baja la preparacion: " + e.getMessage(), e);
        }
    }


    @Override
    public Preparacion modificarDatosDePreparacion(LocalDate nuevaFecha, Long idPreparacion) {
        try {
            if (nuevaFecha == null || nuevaFecha.isBefore(LocalDate.now())) {
                throw new RuntimeException("La nueva fecha no es valida. Debe ser hoy.");
            }

            Preparacion preparacion = preparacionRepository.findById(idPreparacion)
                    .orElseThrow(() -> new RuntimeException("Preparacion no encontrada con id: " + idPreparacion));

            preparacion.setFechaPreparacion(nuevaFecha);
            return preparacionRepository.save(preparacion);

        } catch (Exception e) {
            throw new RuntimeException("error al modificar la fecha de la preparacion: " + e.getMessage(), e);
        }
    }


    @Override
    public List<RecetaActivaDTO> listarRecetasActivas(LocalDate fechaFiltrar, String nombreFiltrar) {
        try {
            List<Preparacion> preparaciones;

            // filtrado de recetas activas
            if (fechaFiltrar != null && nombreFiltrar != null && !nombreFiltrar.trim().isEmpty()) {

                preparaciones = preparacionRepository.findByFechaPreparacion(fechaFiltrar).stream()
                        .filter(p -> p.getSeleccionDeRecetas().stream()
                                .anyMatch(r -> r.getNombre().toLowerCase().contains(nombreFiltrar.toLowerCase())))
                        .toList();

            } else if (fechaFiltrar != null) {
                preparaciones = preparacionRepository.findByFechaPreparacion(fechaFiltrar);
            } else if (nombreFiltrar != null && !nombreFiltrar.trim().isEmpty()) {
                preparaciones = preparacionRepository.findByNombreDeReceta(nombreFiltrar);
            } else {
                preparaciones = preparacionRepository.findByRecetaActivaTrue();
            }

            // Para devolver las recetas activas, generamos un DTO para solo devolver lo que pide el enunciado.
            return preparaciones.stream()
                    .flatMap(p -> p.getSeleccionDeRecetas().stream()
                            .filter(Receta::isActiva)
                            .map(r -> {
                                int caloriasPorPlato = r.getIngredientes().stream()
                                        .filter(IngredienteReceta::isActivo)
                                        .mapToInt(IngredienteReceta::getCalorias)
                                        .sum();

                                return new RecetaActivaDTO(
                                        r.getNombre(),
                                        p.getFechaPreparacion(),
                                        p.getCantidadDeRacionesPreparar(),
                                        caloriasPorPlato
                                );
                            }))
                    .toList();


        } catch (Exception e) {
            throw new RuntimeException("error al listar recetas activas: " + e.getMessage(), e);
        }
    }



}

