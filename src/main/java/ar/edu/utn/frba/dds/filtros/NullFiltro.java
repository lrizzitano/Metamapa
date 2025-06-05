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
    Map<String,String> query = new HashMap<String,String>();
    query.put(this.toString(), "");
    return query;
  }
}
