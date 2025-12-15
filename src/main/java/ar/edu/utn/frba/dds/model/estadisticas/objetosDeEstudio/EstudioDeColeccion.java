package ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.server.configuracion.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EstudioDeColeccion implements ObjetoDeEstudio {

  private RepoColecciones coleccionesRepository;

  public EstudioDeColeccion(RepoColecciones coleccionesRepository) {
    this.coleccionesRepository = coleccionesRepository;
  }

  @Override
  public List<ResultadoEstadistico> estudiar(LocalDateTime desde) {
    return this.estructurarInformacion(desde, this.recolectarDatos());
  }

  public List<ResultadoEstadistico> estructurarInformacion(LocalDateTime desde, Set<Coleccion> informacion) {
    return informacion.stream()
        .map(coleccion -> provinciasPorHecho(desde, coleccion))
        .collect(Collectors.toList());
  }

  public Set<Coleccion> recolectarDatos() {
    Set<Coleccion> informacion = coleccionesRepository.findAll();

    if(informacion == null || informacion.isEmpty() ){
      throw new NoExisteInformacionException("no se encontraron colecciones");
    }

    return informacion;
  }

  public ResultadoEstudioColeccion provinciasPorHecho(LocalDateTime desde, Coleccion coleccion) {
    Map<Provincia,Long> provinciasXcantHechos = coleccion.hechos(new FiltroFechaDesde(desde.withHour(0))).stream()
        .filter(h -> h.getProvincia() != null)
        .collect(Collectors.groupingBy(
            Hecho::getProvincia,
            Collectors.counting()
        ));

    if(provinciasXcantHechos.isEmpty()) {
      new Logger().loggearExcepcion(new NoExisteInformacionException(
          "al buscar los hechos de la coleccion " + coleccion.getTitulo() + " no se encontraron datos"
      ));
    }

    List<HechosPorProvincia> lista = provinciasXcantHechos.entrySet()
        .stream()
        .map(entry -> new HechosPorProvincia(entry.getKey(), entry.getValue()))
        .toList();

    return
        new ResultadoEstudioColeccion(LocalDateTime.now(),
            coleccion,
            provinciasXcantHechos.values().stream().mapToLong(Long::longValue).sum(),
            lista);
  }
}