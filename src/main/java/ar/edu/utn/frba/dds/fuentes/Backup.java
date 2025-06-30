package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class Backup implements Calendarizable{

  public Fuente fuenteDinamica = FuenteDinamica.instance();
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

  @Override
  public void actualizar() { //ESTO ES BACKUPEAR

    Filtro nullFiltro = new NullFiltro();

    Set<Hecho> hechosABackupear = this.fuenteDinamica.obtenerHechos(nullFiltro);
    //SOBREESCRIBIR EL JSON

    this.ultimaActualizacion = LocalDate.now();
  }

}
