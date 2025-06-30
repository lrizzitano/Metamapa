package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Calendarizable;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.FuentesRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CriterioConsenso implements Calendarizable {

  public FuentesRepository fuentesRepository = FuentesRepository.instance();
  public Set<Hecho> hechosConsensuados = new HashSet<>();
  public Duration intervaloActualizacion = Duration.ofMinutes(24);
  public LocalDate ultimaActualizacion;

  @Override
  public Boolean tocaActualizar() {
    //MARGEN DE 5 MINS ANTES O DESPUES.
    Duration transcurrido = Duration.between(ultimaActualizacion, LocalDate.now());

    Duration margenMin = intervaloActualizacion.plus(Duration.ofMinutes(5));
    Duration margenMax = intervaloActualizacion.minus(Duration.ofMinutes(5));

    return (!transcurrido.minus(margenMin).isNegative()) &&
        (transcurrido.compareTo(margenMax) <= 0);
  }

  public Set<Hecho> getHechosConsensuados() {
    return this.hechosConsensuados;
  }
}
