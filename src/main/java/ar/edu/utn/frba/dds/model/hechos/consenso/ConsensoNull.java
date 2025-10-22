package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
@Entity
@DiscriminatorValue("No hay consenso")
public class ConsensoNull extends Consenso {

  public ConsensoNull() {
    super(null, null, null);
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
