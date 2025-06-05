package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class NullFiltro implements Filtro {
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> true;
  }

  public Map<String,String> toQueryParam() {
    return new HashMap<String,String>();
  }
}
