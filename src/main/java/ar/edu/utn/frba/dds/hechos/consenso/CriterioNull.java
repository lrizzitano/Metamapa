package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Set;

public class CriterioNull extends CriterioConsenso{

  @Override
  public boolean esConsensuado(Hecho hecho) {
    return true;
  }

  @Override
  public void actualizar() {
  }

  @Override
  public Set<Hecho> actualizarHechos(Set<Fuente> fuentes) {
    return Set.of();
  }
}
