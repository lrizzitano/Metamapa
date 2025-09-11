package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.hechos.Coleccion;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
  private List<HechosPorProvincia> hechosPorProvincia;

  public ResultadoEstudioColeccion() {

  }
  public ResultadoEstudioColeccion(LocalDateTime now,
                                   Coleccion coleccion,
                                   Long total_hechos,
                                   List<HechosPorProvincia> hechosPorProvincia) {
    this.fecha = now;
    this.coleccion = coleccion;
    this.total_hechos = total_hechos;
    this.hechosPorProvincia = hechosPorProvincia;
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

  public List<HechosPorProvincia> getHechosPorProvincia() {
    return hechosPorProvincia;
  }

  @Override
  public Map<String, String> infoExportable() {
    Map<String, String> datos = new LinkedHashMap<>();

    // campos simples
    datos.put("id", id != null ? id.toString() : "");
    datos.put("fecha", fecha != null ? fecha.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm")) : "");
    datos.put("coleccion_id", coleccion != null && coleccion.getId() != null ? coleccion.getId() : "");
    datos.put("coleccion_nombre", coleccion != null && coleccion.getTitulo() != null ? coleccion.getTitulo() : "");
    datos.put("total_hechos", total_hechos != null ? total_hechos.toString() : "0");

    // abrimos provincias en columnas
    return HechosPorProvincia.abrirProvincias(datos, hechosPorProvincia);
  }
}