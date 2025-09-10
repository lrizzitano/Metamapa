package ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.CategoriaConMasHechos;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.ColeccionesRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoriaConMasHechosReportados implements ObjetoDeEstudio {

  ColeccionesRepository coleccionesRepository;

  CategoriaConMasHechosReportados(ColeccionesRepository coleccionesRepository) {
    this.coleccionesRepository = coleccionesRepository;
  }

  @Override
  public List<ResultadoEstadistico> estudiar() {
    return this.calcularEstadisticas(this.transformarDatosEnInformacion());
  }

  private List<ResultadoEstadistico> calcularEstadisticas(List<Hecho> informacion) {
    Map.Entry<String,Long> categoria = informacion.stream().collect(Collectors.groupingBy(
            Hecho::categoria,
            Collectors.counting()
        ))
        .entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .orElse(null);

    if (categoria == null || categoria.getValue() == null || categoria.getKey() == null) {
      throw new NoExisteInformacionException("no existe una categoria");
    }

    return List.of(new CategoriaConMasHechos(LocalDate.now(), categoria.getKey(), categoria.getValue()));
  }

  private List<Hecho> transformarDatosEnInformacion() {
    List<Hecho> informacion = coleccionesRepository.findAll().stream()
        .map(coleccion -> coleccion.hechos(new NullFiltro()))
        .flatMap(Collection::stream)
        .toList();

    if (informacion.isEmpty()) {
      throw new NoExisteInformacionException("no se encontraron datos al buscar colecciones o hechos");
    }
    return informacion;
  }
}