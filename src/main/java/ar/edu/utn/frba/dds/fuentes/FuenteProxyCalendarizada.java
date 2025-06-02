package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;

import static java.lang.Thread.sleep;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class FuenteProxyCalendarizada implements Fuente {
  private final Duration refreshTime;
  private final Set<Hecho> hechos = ConcurrentHashMap.newKeySet();

  public FuenteProxyCalendarizada(Duration refreshTime) {
    this.refreshTime = refreshTime;
  }

  public void init() {
    new Thread(this::acumularHechos).start();
  }

  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return this.hechos.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }

  private void acumularHechos() {
    Instant ultimaLlamada = null;
    while (true){
      hechos.addAll(getNewHechos(ultimaLlamada));
      ultimaLlamada = Instant.now();
      try {
        //noinspection BusyWait
        sleep(refreshTime.toMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  protected abstract Set<Hecho> getNewHechos(Instant ultimaLlamada);

}
