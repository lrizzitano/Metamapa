package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.hechos.Coleccion;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "EstudioColeccion")
public class ResultadoEstudioColeccion implements ResultadoEstadistico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "estudio_id")
  private Long id;
  @Column(name = "fecha")
  private LocalDateTime fecha;
  @ManyToOne()
  @JoinColumn(name = "coleccion_id")
  private Coleccion coleccion;
  @Column(name = "total_hechos")
  private Long total_hechos;
  @ElementCollection(fetch = FetchType.EAGER)
  private List<HechosPorProvincia> hechosXColecciones;


  public ResultadoEstudioColeccion() {

  }
  public ResultadoEstudioColeccion(LocalDateTime now,
                                   Coleccion coleccion,
                                   Long total_hechos,
                                   List<HechosPorProvincia> hechosXColecciones) {
    this.fecha = now;
    this.coleccion = coleccion;
    this.total_hechos = total_hechos;
    this.hechosXColecciones = hechosXColecciones;
  }

  public LocalDateTime getFecha() {
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
