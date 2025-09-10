package ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.ColeccionesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstudioDeColeccion implements ObjetoDeEstudio {

  private ColeccionesRepository coleccionesRepository;

  public EstudioDeColeccion(ColeccionesRepository coleccionesRepository) {
    this.coleccionesRepository = coleccionesRepository;
  }

  @Override
  public List<ResultadoEstadistico> estudiar(LocalDate desde) {
    return this.estructurarInformacion(desde, this.recolectarDatos());
  }

  private List<ResultadoEstadistico> estructurarInformacion(LocalDate desde, List<Coleccion> informacion) {
    return informacion.stream()
        .map(coleccion -> pronvinciaConMasHechos(desde, coleccion))
        .collect(Collectors.toList());
  }

  private List<Coleccion> recolectarDatos() {
    List<Coleccion> informacion = coleccionesRepository.findAll();

    if(informacion.isEmpty()) {
      throw new NoExisteInformacionException("no se encontraron colecciones");
    }

    return informacion;
  }

  private ResultadoEstadistico pronvinciaConMasHechos(LocalDate desde, Coleccion coleccion) {
    Provincia provincia = coleccion.hechos(new FiltroFechaDesde(desde)).stream()
        .collect(Collectors.groupingBy(
            Hecho::getProvincia,
            Collectors.counting()
        ))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);

    if(provincia == null) {
      throw new NoExisteInformacionException(
          "al buscar los hechos de la coleccion " + coleccion.getTitulo() + " no se encontraron datos"
      );
    }

    return new ResultadoEstudioColeccion(LocalDate.now(), coleccion, provincia);
  }
}