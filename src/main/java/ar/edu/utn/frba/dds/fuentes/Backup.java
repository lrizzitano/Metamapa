package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public class Backup implements Calendarizable {

  public Fuente fuenteDinamica = FuenteDinamica.instance();
  public Duration frecuencia = Duration.ofDays(1);
  public LocalDateTime ultimaActualizacion = LocalDateTime.now();

  @Override
  public LocalDateTime ultimaActualizaion() {
    return this.ultimaActualizacion;
  }

  @Override
  public Duration frecuencia() {
    return this.frecuencia;
  }

  @Override
  public void actualizar() { //ESTO ES BACKUPEAR

    Filtro nullFiltro = new NullFiltro();

    Set<Hecho> hechosABackupear = this.fuenteDinamica.obtenerHechos(nullFiltro);
    //SOBREESCRIBIR EL JSON

    this.ultimaActualizacion = LocalDateTime.now();
  }

}
