package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.fuentes.FuentesRepository;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Consenso implements Calendarizable {
  private final FuentesRepository fuentes = FuentesRepository.instance();
  private final AlgoritmoConsenso algoritmoConsenso;
  private Set<Hecho> hechosConsensuados = new HashSet<>();
  private LocalDate proximaActualizacion;

  public Consenso(AlgoritmoConsenso algoritmoConsenso, LocalDate proximaActualizacion) {
    this.algoritmoConsenso = algoritmoConsenso;
    this.proximaActualizacion = proximaActualizacion;
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return proximaActualizacion.atStartOfDay().plusHours(3);
  }

  @Override
  public void actualizar() {
    this.hechosConsensuados = algoritmoConsenso.getHechosConsensuados(fuentes.getFuentes());
    this.proximaActualizacion = LocalDate.now().plusDays(1);
  }

  public Set<Hecho> getHechosConsensuados() {
    return this.hechosConsensuados;
  }

  public boolean esConsensuado(Hecho hecho) {
    return this.hechosConsensuados.contains(hecho);
  }
}
