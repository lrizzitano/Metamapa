package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class Filtro {

  abstract public Predicate<Hecho> getAsPredicate();

  abstract public Map<String, String> toQueryParam();

}



