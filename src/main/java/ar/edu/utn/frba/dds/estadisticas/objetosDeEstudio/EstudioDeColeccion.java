package ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.hechos.Provincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepoColecciones;

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
        .map(coleccion -> pronvinciaConMasHechos(desde, coleccion))
        .collect(Collectors.toList());
  }

  public Set<Coleccion> recolectarDatos() {
    Set<Coleccion> informacion = coleccionesRepository.findAll();

    if(informacion == null || informacion.isEmpty() ){
      throw new NoExisteInformacionException("no se encontraron colecciones");
    }

    return informacion;
  }

  public ResultadoEstudioColeccion pronvinciaConMasHechos(LocalDateTime desde, Coleccion coleccion) {
    Map<Provincia,Long> provinciasXcantHechos = coleccion.hechos(new FiltroFechaDesde(desde.withHour(0))).stream()
        .filter(h -> h.getProvincia() != null)
        .collect(Collectors.groupingBy(
            Hecho::getProvincia,
            Collectors.counting()
        ));

    if(provinciasXcantHechos.isEmpty()) {
      throw new NoExisteInformacionException(
          "al buscar los hechos de la coleccion " + coleccion.getTitulo() + " no se encontraron datos"
      );
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