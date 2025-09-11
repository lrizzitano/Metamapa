package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.hechos.Provincia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ResultadoEstudioCategoria implements  ResultadoEstadistico{

  private final LocalDateTime fecha;
  private final String categoria;
  private final int total_hechos;
  private final LocalTime hora_pico;
  private final List<HechosPorProvincia> hechosPorProvincia;

  public ResultadoEstudioCategoria(LocalDateTime fecha,
                                   String categoria,
                                   int total_hechos,
                                   double hora_pico_minutos,
                                   List<HechosPorProvincia> hechosPorProvincia) {
    this.fecha = fecha;
    this.categoria = categoria;
    this.total_hechos = total_hechos;
    this.hora_pico = LocalTime.of((int) hora_pico_minutos / 60, (int) hora_pico_minutos % 60);
    this.hechosPorProvincia =  hechosPorProvincia;
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

  public List<HechosPorProvincia> getHechosPorProvincia() {
    return hechosPorProvincia;
  }
}
