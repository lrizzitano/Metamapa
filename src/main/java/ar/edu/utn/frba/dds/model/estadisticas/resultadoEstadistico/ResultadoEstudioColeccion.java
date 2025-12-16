package ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "resultadoEstadistico_id")
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

  public String getFecha() {
    return fecha == null ? "Sin fecha"
        : DateTimeFormatter.ofPattern("dd/MM/yy").format(fecha);
  }

  public Coleccion getColeccion() {
    return coleccion;
  }

  public String titulo() {
    return this.coleccion.getTitulo();
  }

  public Long total_hechos() {
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
    datos.put("fecha", fecha != null ? fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
    datos.put("coleccion_id", coleccion != null && coleccion.getId() != null ? coleccion.getId().toString() : "");
    datos.put("coleccion_nombre", coleccion != null && coleccion.getTitulo() != null ? coleccion.getTitulo() : "");
    datos.put("total_hechos", total_hechos != null ? total_hechos.toString() : "0");

    // abrimos provincias en columnas
    return HechosPorProvincia.abrirProvincias(datos, hechosPorProvincia);
  }
}