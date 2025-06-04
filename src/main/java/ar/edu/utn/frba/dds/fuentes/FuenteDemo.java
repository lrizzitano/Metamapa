package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FuenteDemo extends FuenteProxyCalendarizada {
  private final URL url;
  private final Conexion conexion;

  public FuenteDemo(Conexion conexion, URL url) {
    this.conexion = conexion;
    this.url = url;
    this.iniciar();
  }


  @Override
  protected Set<Hecho> getNewHechos(Instant ultimaLlamada) {
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
