package ar.edu.utn.frba.dds.fuentes.demo;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("demo")
public class FuenteDemo extends Fuente implements Calendarizable {

  public FuenteDemo(){}

  @Column(name = "url_demo")
  private  URL url;

  @Transient
  private  Conexion conexion;

  @OneToMany
  @JoinColumn(name = "hechos_demo")
  private final Set<Hecho> hechos = ConcurrentHashMap.newKeySet();

  @Column(name = "ultimaActualizacion_demo")
  private LocalDateTime ultimaActualizacion;

  public FuenteDemo(Conexion conexion, URL url, LocalDateTime ultimaActualizacion) {
    this.conexion = conexion;
    this.url = url;
    this.ultimaActualizacion = ultimaActualizacion;
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return this.hechos.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return this.ultimaActualizacion.plusHours(1);
  }

  @Override
  public void actualizar() {
    hechos.addAll(this.getNewHechos(ultimaActualizacion.
        atZone(ZoneId.systemDefault()).toInstant()));
    this.ultimaActualizacion = LocalDateTime.now();
  }

  private Set<Hecho> getNewHechos(Instant ultimaLlamada) {
    Set<Hecho> hechos = new HashSet<>();
    Map<String, Object> fila;
    while ((fila = conexion.siguienteHecho(url, ultimaLlamada)) != null) {
      hechos.add(this.parseHecho(fila));
    }
    return hechos;
  }

  private Hecho parseHecho(Map<String, Object> fila) {
    return new Hecho(
        null, // creo que tiene sentido que un hecho de una fuente externa tenga id en null
        (String) fila.get("titulo"),
        (String) fila.get("descripcion"),
        (String) fila.get("categoria"),
        (Double) fila.get("latitud"),
        (Double) fila.get("longitud"),
        LocalDate.now(),
        (LocalDate) fila.get("fecha"),
        Origen.MANUAL
    );
  }
}
