package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.fuentes.FuentesRepository;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public abstract class CriterioConsenso implements Calendarizable {

  public FuentesRepository fuentesRepository = FuentesRepository.instance();
  public Set<Hecho> hechosConsensuados = new HashSet<>();
  public Duration frecuencia = Duration.ofDays(1);
  public LocalDateTime ultimaActualizacion;


  public CriterioConsenso(){
    this.actualizar();
  }

  @Override
  public LocalDateTime ultimaActualizaion() {
    return this.ultimaActualizacion;
  }

  @Override
  public Duration frecuencia() {
    return this.frecuencia;
  }

  public Set<Hecho> getHechosConsensuados() {
    return this.hechosConsensuados;
  }
}
