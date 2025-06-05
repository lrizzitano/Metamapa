package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class FuenteProxyCalendarizada implements Fuente {
  private final Set<Hecho> hechos = ConcurrentHashMap.newKeySet();

  public FuenteProxyCalendarizada(Duration frecuencia) {
    this.frecuencia = frecuencia;
  }

  public Duration getFrecuencia() {
    return frecuencia;
  }

  public void actualizarHechos(Instant ultimaLlamada) {
    hechos.addAll(getNewHechos(ultimaLlamada));
  }

  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return this.hechos.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }

  protected abstract Set<Hecho> getNewHechos(Instant ultimaLlamada);

}
