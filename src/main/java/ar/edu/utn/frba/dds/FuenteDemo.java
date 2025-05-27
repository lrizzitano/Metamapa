package ar.edu.utn.frba.dds;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FuenteDemo extends FuenteProxyAsincronica {

  private final Conexion conexion;

  public FuenteDemo(Conexion conexion, URL url, Duration refreshTime) {
    super(url, refreshTime);
    this.conexion = conexion;
    this.init();
  }


  @Override
  protected List<Hecho> getNextHecho(Instant ultimaLlamada) {
    return List.of(this.parseHecho(conexion.siguienteHecho(url, ultimaLlamada)));
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
