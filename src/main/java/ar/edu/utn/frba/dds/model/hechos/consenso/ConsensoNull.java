package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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

  public String getNombre(){
    return "Consenso Nulo";
  }
}
