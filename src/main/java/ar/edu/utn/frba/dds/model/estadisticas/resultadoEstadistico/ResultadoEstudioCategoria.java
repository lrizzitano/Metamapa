package ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "EstudioCategoria")
public class ResultadoEstudioCategoria implements ResultadoEstadistico {

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
  @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
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

  public String getFecha() {
    return fecha == null ? "Sin fecha"
        : DateTimeFormatter.ofPattern("dd/MM/yy").format(fecha);
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

  @Override
  public Map<String, String> infoExportable() {
    Map<String, String> datos = new LinkedHashMap<>(); // mantiene orden de insercion

    // campos simples
    datos.put("id", id != null ? id.toString() : "");
    datos.put("fecha", fecha != null ? fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "");
    datos.put("categoria", categoria != null ? categoria : "");
    datos.put("total_hechos", String.valueOf(total_hechos));
    datos.put("hora_pico", hora_pico != null ? hora_pico.toString() : "");

    // abro las provincias en columnas
    return HechosPorProvincia.abrirProvincias(datos, hechosPorProvincia);
  }
}
