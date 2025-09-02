package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Map;
import java.util.function.Predicate;

public class NullFiltro extends Filtro {

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> true;
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of();
  }
}
