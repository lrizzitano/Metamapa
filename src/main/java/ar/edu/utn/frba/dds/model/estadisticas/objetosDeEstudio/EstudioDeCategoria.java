package ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class EstudioDeCategoria implements ObjetoDeEstudio {

  private final RepoColecciones coleccionesRepository;

  public EstudioDeCategoria(RepoColecciones coleccionesRepository) {
    this.coleccionesRepository = coleccionesRepository;
  }

  @Override
  public List<ResultadoEstadistico> estudiar(LocalDateTime desde) {
    return this.estructurarInformacion(this.recolectarDatos(desde), desde);
  }

  private List<ResultadoEstadistico> estructurarInformacion(List<Hecho> informacion, LocalDateTime desde) {

    Map<String, ResultadoEstadistico> mapResultados = informacion.stream()
        .collect(groupingBy(
            Hecho::categoria, //clasificador, agrupa por esto, en analizar categoria se guarda la misma en el resultado
            collectingAndThen(
                toList(),
                hechosCategoria -> analizarHechosDeCategoria(hechosCategoria, desde)
                )
        ));

    if (!mapResultados.isEmpty()) {
      new Logger().info("Se encontraron datos al buscar colecciones o hechos");
    }

    return new ArrayList<>(mapResultados.values());
  }

  private List<Hecho> recolectarDatos(LocalDateTime desde) {
    var filtro = new FiltroCompuesto(List.of(
        new FiltroFechaDesde(desde.withHour(0)),
        new FiltroFechaHasta(desde.plusDays(1).withHour(0))));
    List<Hecho> informacion = coleccionesRepository.findAll().stream()
        .map(coleccion -> coleccion.setSolicitudes(new SolicitudesDeEliminacionJPA()))
        .map(coleccion -> coleccion.hechos(filtro))
        .flatMap(Collection::stream)
        .toList();

    if (!informacion.isEmpty()) {
       new Logger().info("Se encontraron datos al buscar colecciones o hechos");
    }
    return informacion;
  }

  private ResultadoEstadistico analizarHechosDeCategoria(List<Hecho> hechosCategoria, LocalDateTime desde) {

    // setear la categoría
    String categoria = hechosCategoria.isEmpty() ? null : hechosCategoria.get(0).categoria();

    // total de hechos
    int totalHechos = hechosCategoria.size();

    // promedio horario en minutos
    double pico_de_subida = hechosCategoria.stream()
        .mapToInt(h -> h.fechaCarga().getHour() * 60 + h.fechaCarga().getMinute())
        .average()
        .orElse(0);

    // provincia x hechos
    Map<Provincia, Long> conteoProvincia = hechosCategoria.stream()
        .collect(groupingBy(Hecho::getProvincia, counting()));

    List<HechosPorProvincia> listaHechosXProvincia = conteoProvincia.entrySet()
        .stream()
        .map(entry -> new HechosPorProvincia(entry.getKey(), entry.getValue()))
        .toList();

    return new ResultadoEstudioCategoria
        (desde, categoria, totalHechos, pico_de_subida, listaHechosXProvincia);
  }
}