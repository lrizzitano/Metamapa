package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDateTime;

public class ConsensoNull extends Consenso {

  public ConsensoNull() {
    super(null, null);
  }

  @Override
  public boolean esConsensuado(Hecho hecho) {
    return true;
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return LocalDateTime.MAX;
  }

  @Override
  public void actualizar() {
  }
}
