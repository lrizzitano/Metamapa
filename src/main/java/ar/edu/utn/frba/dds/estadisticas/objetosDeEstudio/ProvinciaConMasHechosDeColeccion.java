package ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.estadisticas.LocalizadorAdapter;
import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ProvinciaConMasHechos;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.ColeccionesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProvinciaConMasHechosDeColeccion implements ObjetoDeEstudio {

  private ColeccionesRepository coleccionesRepository;

  public ProvinciaConMasHechosDeColeccion(ColeccionesRepository coleccionesRepository) {
    this.coleccionesRepository = coleccionesRepository;
  }

  @Override
  public List<ResultadoEstadistico> estudiar() {
    return this.calcularEstadisticas(this.transformarDatosEnInformacion());
  }

  private List<ResultadoEstadistico> calcularEstadisticas(List<Coleccion> informacion) {
    return informacion.stream()
        .map(this::pronvinciaConMasHechos)
        .collect(Collectors.toList());
  }

  private List<Coleccion> transformarDatosEnInformacion() {
    List<Coleccion> informacion = coleccionesRepository.findAll();

    if(informacion.isEmpty()) {
      throw new NoExisteInformacionException("no se encontraron colecciones");
    }

    return informacion;
  }

  private ResultadoEstadistico pronvinciaConMasHechos(Coleccion coleccion) {
    Provincia provincia = coleccion.hechos(new NullFiltro()).stream()
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

    return new ProvinciaConMasHechos(LocalDate.now(), coleccion, provincia);
  }
}