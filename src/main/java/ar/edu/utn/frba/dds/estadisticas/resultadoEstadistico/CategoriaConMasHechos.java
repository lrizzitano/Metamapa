package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.Categoria;

import java.time.LocalDate;

public class CategoriaConMasHechos implements ResultadoEstadistico {

  private LocalDate fecha;
  private String categoria;
  private Long cantidadReportada;

  public CategoriaConMasHechos(LocalDate now, String categoria, Long cantidadReportada) {
    this.fecha = now;
    this.categoria = categoria;
    this.cantidadReportada = cantidadReportada;
  }
}
