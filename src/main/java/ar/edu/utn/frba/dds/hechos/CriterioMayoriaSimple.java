package ar.edu.utn.frba.dds.hechos;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.Set;

public class CriterioMayoriaSimple extends CriterioConsenso{

  @Override
  public void actualizar() {
    Set<Fuente> fuentes = this.fuentesRepository.getFuentes();

  }
}
