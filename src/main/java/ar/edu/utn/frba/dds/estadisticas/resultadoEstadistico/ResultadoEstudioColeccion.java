package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.hechos.Coleccion;

import java.time.LocalDate;
import java.util.List;

public class ResultadoEstudioColeccion implements ResultadoEstadistico {

  private LocalDate fecha;
  private Coleccion coleccion;
  private Long total_hechos;
  private List<HechosPorProvincia> hechosXColecciones;

  public ResultadoEstudioColeccion(LocalDate now,
                                   Coleccion coleccion,
                                   Long total_hechos,
                                   List<HechosPorProvincia> hechosXColecciones) {
    this.fecha = now;
    this.coleccion = coleccion;
    this.total_hechos = total_hechos;
    this.hechosXColecciones = hechosXColecciones;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public Coleccion getColeccion() {
    return coleccion;
  }

  public Long getTotal_hechos() {
    return total_hechos;
  }

  public List<HechosPorProvincia> getHechosXColecciones() {
    return hechosXColecciones;
  }
}
