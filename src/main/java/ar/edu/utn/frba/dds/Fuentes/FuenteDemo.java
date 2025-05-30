package ar.edu.utn.frba.dds.Fuentes;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Hechos.Origen;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FuenteDemo extends FuenteProxyAsincronica {
  private final URL url;
  private final Conexion conexion;

  public FuenteDemo(Conexion conexion, URL url) {
    super(Duration.ofHours(1));
    this.conexion = conexion;
    this.url = url;
    this.init();
  }


  @Override
  protected List<Hecho> getNextHecho(Instant ultimaLlamada) {
    return Collections.singletonList(this.parseHecho(conexion.siguienteHecho(url, ultimaLlamada)));
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
