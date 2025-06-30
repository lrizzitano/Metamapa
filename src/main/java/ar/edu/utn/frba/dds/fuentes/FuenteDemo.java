package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FuenteDemo implements Fuente, Calendarizable{
  private final URL url;
  private final Conexion conexion;
  private final Set<Hecho> hechos = ConcurrentHashMap.newKeySet();
  private LocalDateTime ultimaActualizacion;
  private final Duration frecuencia = Duration.ofHours(1);

  public FuenteDemo(Conexion conexion, URL url) {
    this.conexion = conexion;
    this.url = url;
    this.ultimaActualizacion = LocalDateTime.now();
    this.actualizar();
  }

  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return this.hechos.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }

  @Override
  public Boolean tocaActualizar() {
    Duration transcurrido = Duration.between(ultimaActualizacion, LocalDateTime.now());

    return (!transcurrido.minus( frecuencia.plus(Duration.ofMinutes(5))).isNegative()) &&
        (transcurrido.compareTo( frecuencia.plus(Duration.ofMinutes(5))) <= 0);
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
