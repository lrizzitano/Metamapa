package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.FuentesRepository;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public abstract class CriterioConsenso implements Calendarizable {
  private final FuentesRepository fuentesRepository = FuentesRepository.instance();
  protected Set<Hecho> hechosConsensuados = new HashSet<>();
  private final Duration frecuencia = Duration.ofDays(1);
  protected LocalDateTime ultimaActualizacion;

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

  @Override
  public void actualizar() {
    this.hechosConsensuados = this.actualizarHechos(fuentesRepository.getFuentes());
    this.ultimaActualizacion = LocalDateTime.now();
  }

  protected abstract Set<Hecho> actualizarHechos(Set<Fuente> fuentes);

  public Set<Hecho> getHechosConsensuados() {
    return this.hechosConsensuados;
  }

  public boolean esConsensuado(Hecho hecho) {
    return this.hechosConsensuados.contains(hecho);
  }
}
