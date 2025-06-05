package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface Filtro {
  public Predicate<Hecho> getAsPredicate();

  public Map<String,String> toQueryParam();
}



