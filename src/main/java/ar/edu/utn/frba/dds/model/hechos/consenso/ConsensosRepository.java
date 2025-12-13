package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.repositorios.RepoGenerico;
import java.util.Set;

public class ConsensosRepository extends RepoGenerico<Consenso> {
  public ConsensosRepository() {
    super(Consenso.class);
  }

  // esto es medio type-test, en proximas iteraciones buscarle mejor manera
  public Consenso getConsenso(TipoConsenso consenso) {
    Set<Consenso> consensos = super.findAll();

    return switch (consenso) {
      case ABSOLUTO -> consensos.stream()
          .filter(c -> c.getAlgoritmoConsenso() instanceof AlgoritmoConsensoAbsoluto)
          .findFirst().orElse(new ConsensoNull());
      case MAYORIA_SIMPLE -> consensos.stream()
          .filter(c -> c.getAlgoritmoConsenso() instanceof AlgoritmoMayoriaSimple)
          .findFirst().orElse(new ConsensoNull());
      case MULTIPLES_MENCIONES -> consensos.stream()
          .filter(c -> c.getAlgoritmoConsenso() instanceof AlgoritmoMultiplesMenciones)
          .findFirst().orElse(new ConsensoNull());
      case NULO -> new ConsensoNull();
    };

  }
}
