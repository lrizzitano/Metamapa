package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.hechos.Coleccion;

import java.time.LocalDate;

public class ResultadoEstudioColeccion implements ResultadoEstadistico {

  LocalDate fecha;
  Coleccion coleccion;
  Provincia provincia;

  public ResultadoEstudioColeccion(LocalDate now, Coleccion coleccion, Provincia provincia) {
    this.fecha = now;
    this.coleccion = coleccion;
    this.provincia = provincia;
  }
}
