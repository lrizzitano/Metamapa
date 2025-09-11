package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.hechos.Provincia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ResultadoEstudioCategoria implements  ResultadoEstadistico{

  private final LocalDateTime fecha;
  private final String categoria;
  private final int total_hechos;
  private final LocalTime hora_pico;
  private final Provincia provincia_con_mas_reportes;
  private final Long total_hechos_provincia;

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

  public LocalDateTime getFecha() {
    return fecha;
  }

  public String getCategoria() {
    return categoria;
  }

  public int getTotal_hechos() {
    return total_hechos;
  }

  public LocalTime getHora_pico() {
    return hora_pico;
  }

  public Provincia getProvincia_con_mas_reportes() {
    return provincia_con_mas_reportes;
  }

  public Long getTotal_hechos_provincia() {
    return total_hechos_provincia;
  }
}
