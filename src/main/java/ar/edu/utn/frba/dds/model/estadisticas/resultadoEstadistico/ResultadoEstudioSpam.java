package ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EstudioSpam")
public class ResultadoEstudioSpam implements ResultadoEstadistico {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "estudio_id")
  private Long id;

  @Column(name = "fecha")
  private LocalDateTime fecha;

  @Column(name = "total_solicitudes")
  private Long total_solicitudes;

  @Column(name = "total_spam")
  private Long total_spam;

  public ResultadoEstudioSpam() {}

  public ResultadoEstudioSpam(LocalDateTime now,
                                   Long total_solicitudes,
                                   Long total_spam) {
    this.fecha = now;
    this.total_solicitudes = total_solicitudes;
    this.total_spam = total_spam;
  }

  @Override
  public Map<String, String> infoExportable() {
    Map<String, String> datos = new LinkedHashMap<>();

    // campos simples
    datos.put("id", id != null ? id.toString() : "");
    datos.put("fecha", fecha != null ? fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
    datos.put("total_solicitudes", total_solicitudes != null ? total_solicitudes.toString() : "0");
    datos.put("total_spam", total_spam != null ? total_spam.toString() : "0");

    return datos;
  }
}
