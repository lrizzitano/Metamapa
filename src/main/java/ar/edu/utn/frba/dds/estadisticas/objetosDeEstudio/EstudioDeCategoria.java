package ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.ColeccionesRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class EstudioDeCategoria implements ObjetoDeEstudio {

  ColeccionesRepository coleccionesRepository;

  EstudioDeCategoria(ColeccionesRepository coleccionesRepository) {
    this.coleccionesRepository = coleccionesRepository;
  }

  @Override
  public List<ResultadoEstadistico> estudiar(LocalDate desde) {
    return this.calcularEstadisticas(this.transformarDatosEnInformacion(desde));
  }

  private List<ResultadoEstadistico> calcularEstadisticas(List<Hecho> informacion) {

    Map<String, ResultadoEstadistico> mapResultados = informacion.stream()
        .collect(groupingBy(
            Hecho::categoria, //clasificador, agrupa por esto, en analizar categoria se guarda la misma en el resultado
            collectingAndThen(
                toList(),
                this::analizarCategoria
            )
        ));

    List<ResultadoEstadistico> resultados = new ArrayList<>(mapResultados.values());

    return resultados;
  }

  private List<Hecho> transformarDatosEnInformacion(LocalDate desde) {
    List<Hecho> informacion = coleccionesRepository.findAll().stream()
        .map(coleccion -> coleccion.hechos(new FiltroFechaDesde(desde)))
        .flatMap(Collection::stream)
        .toList();

    if (informacion.isEmpty()) {
      throw new NoExisteInformacionException("no se encontraron datos al buscar colecciones o hechos");
    }
    return informacion;
  }

  private ResultadoEstadistico analizarCategoria(List<Hecho> hechosCategoria) {

    // setear la categoría
    String categoria = hechosCategoria.isEmpty() ? null : hechosCategoria.get(0).categoria();

    // total de hechos
    int totalHechos = hechosCategoria.size();

    // promedio horario en minutos
    double promedioHorarioMinutos = hechosCategoria.stream()
        .mapToInt(h -> h.getHora() * 60 + h.getMinuto())
        .average()
        .orElse(0);

    // provincia con más hechos
    Map<Provincia, Long> conteoProvincia = hechosCategoria.stream()
        .collect(groupingBy(Hecho::getProvincia, counting()));

    Map.Entry<Provincia, Long> maxProvincia = conteoProvincia.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .orElse(null);

    if (maxProvincia == null) {
      throw new NoExisteInformacionException("");
    }

    Provincia provincia = maxProvincia.getKey();
    Long totalHechosProvincia = maxProvincia.getValue();

    return new ResultadoEstudioCategoria
        (LocalDate.now(), categoria, totalHechos, promedioHorarioMinutos, provincia, totalHechosProvincia);
  }
}