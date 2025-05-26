package ar.edu.utn.frba.dds;

import static java.lang.Thread.sleep;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class FuenteProxyAsincronica implements Fuente {
  protected final URL url;
  private final Duration refreshTime;
  private final Set<Hecho> hechos = ConcurrentHashMap.newKeySet();

  public FuenteProxyAsincronica(URL url, Duration refreshTime) {
    this.url = url;
    this.refreshTime = refreshTime;
  }

  protected void init() {
    new Thread(this::acumularHechos).start();
  }

  public Set<Hecho> obtenerHechos(Predicate<Hecho> filtro) {
    return this.hechos.stream().filter(filtro).collect(Collectors.toSet());
  }

  private void acumularHechos() {
    List<Hecho> hechosNuevos;
    Instant ultimaLlamada = null;
    while (true) {
      while ((hechosNuevos = this.getNextHecho(ultimaLlamada)) != null) {
        hechos.addAll(hechosNuevos);
      }
      ultimaLlamada = Instant.now();
      try {
        //noinspection BusyWait
        sleep(refreshTime.toMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  protected abstract List<Hecho> getNextHecho(Instant ultimaLlamada);

}
