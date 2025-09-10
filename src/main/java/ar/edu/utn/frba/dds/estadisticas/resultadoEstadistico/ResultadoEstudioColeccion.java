package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.hechos.Coleccion;

import java.time.LocalDate;

public class ResultadoEstudioColeccion implements ResultadoEstadistico {

  private LocalDate fecha;
  private Coleccion coleccion;
  private Provincia provincia;

  public ResultadoEstudioColeccion(LocalDate now, Coleccion coleccion, Provincia provincia) {
    this.fecha = now;
    this.coleccion = coleccion;
    this.provincia = provincia;
  }
}
