package ar.edu.utn.frba.dds.filtros;


import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class FiltroCategoria implements Filtro {
  private final String categoria;

  public FiltroCategoria(String categoria) {
    this.categoria = categoria;
  }


  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.categoria().equals(categoria);
  }

  public Map<String,String> toQueryParam() {
    Map<String,String> query = new HashMap<String,String>();
    query.put(this.toString(), categoria);
    return query;
  }
}