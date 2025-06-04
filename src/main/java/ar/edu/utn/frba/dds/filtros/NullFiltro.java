package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.function.Predicate;

public class NullFiltro implements Filtro {
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> true;
  }

  public String toQueryParam(String prefix, String deliimiter, String suffix) {
    return "";
  }
}
