package ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EstudioDeColeccion implements ObjetoDeEstudio {

  private final RepoColecciones coleccionesRepository;

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

    if(informacion.isEmpty() ){
      new Logger().info("se encontro algo");
    }

    return informacion;
  }

  public ResultadoEstudioColeccion provinciasPorHecho(LocalDateTime desde, Coleccion coleccion) {
    var filtro = new FiltroCompuesto(List.of(
        new FiltroFechaDesde(desde.withHour(0)),
        new FiltroFechaHasta(desde.plusDays(1).withHour(0))));

    Map<Provincia,Long> provinciasXcantHechos = coleccion.hechos(filtro).stream()
        .filter(h -> h.getProvincia() != null)
        .collect(Collectors.groupingBy(
            Hecho::getProvincia,
            Collectors.counting()
        ));

    if (!provinciasXcantHechos.isEmpty()) {
      new Logger().info("Encontre algo");

    }

    List<HechosPorProvincia> lista = provinciasXcantHechos.entrySet()
        .stream()
        .map(entry -> new HechosPorProvincia(entry.getKey(), entry.getValue()))
        .toList();

    return
        new ResultadoEstudioColeccion(desde,
            coleccion,
            provinciasXcantHechos.values().stream().mapToLong(Long::longValue).sum(),
            lista);
  }
}