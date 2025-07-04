package ar.edu.utn.frba.dds.fuentes.demo;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
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

public class FuenteDemo implements Fuente, Calendarizable {
  private final URL url;
  private final Conexion conexion;
  private final Set<Hecho> hechos = ConcurrentHashMap.newKeySet();
  private LocalDateTime ultimaActualizacion;

  public FuenteDemo(Conexion conexion, URL url, LocalDateTime ultimaActualizacion) {
    this.conexion = conexion;
    this.url = url;
    this.ultimaActualizacion = ultimaActualizacion;
  }

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
