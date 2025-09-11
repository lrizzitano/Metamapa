package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "EstudioCategoria")
public class ResultadoEstudioCategoria implements  ResultadoEstadistico{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "estudio_id")
  private Long id;
  @Column(name = "fecha")
  private LocalDateTime fecha;
  @Column(name = "categoria")
  private String categoria;
  @Column(name = "total_hechos")
  private int total_hechos;
  @Column(name = "hora_pico")
  private LocalTime hora_pico;
  @ElementCollection(fetch =  FetchType.EAGER)
  private List<HechosPorProvincia> hechosPorProvincia;

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
  public ResultadoEstudioCategoria() {

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
