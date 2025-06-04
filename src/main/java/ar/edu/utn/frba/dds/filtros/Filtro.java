package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.function.Predicate;

public interface Filtro {
  public Predicate<Hecho> getAsPredicate();

  public String toQueryParam(String prefix, String delimiter, String suffix);
}



