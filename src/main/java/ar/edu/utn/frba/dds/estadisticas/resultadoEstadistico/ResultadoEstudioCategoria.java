package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.hechos.Provincia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ResultadoEstudioCategoria implements  ResultadoEstadistico{

  private LocalDateTime fecha;
  private String categoria;
  private int total_hechos;
  private LocalTime hora_pico;
  private Provincia provincia_con_mas_reportes;
  private Long total_hechos_provincia;

  public ResultadoEstudioCategoria(LocalDateTime fecha,
                                   String categoria,
                                   int total_hechos,
                                   double hora_pico_minutos,
                                   Provincia provincia_con_mas_reportes,
                                   Long total_hechos_provincia) {
    this.fecha = fecha;
    this.categoria = categoria;
    this.total_hechos = total_hechos;
    this.hora_pico = LocalTime.of((int) hora_pico_minutos / 60, (int) hora_pico_minutos % 60);
    this.provincia_con_mas_reportes = provincia_con_mas_reportes;
    this.total_hechos_provincia = total_hechos_provincia;
  }
}
