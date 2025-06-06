package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FiltroCompuesto implements Filtro {
  private final List<Filtro> filtros;

  public FiltroCompuesto() {
    filtros = new ArrayList<>();
  }

  public FiltroCompuesto(List<Filtro> filtros) {
    this.filtros = new ArrayList<>(filtros);
  }

  public FiltroCompuesto and(Filtro filtro) {
    this.filtros.add(filtro);
    return this;
  }

  public Predicate<Hecho> getAsPredicate() {
    return filtros.stream().map(Filtro::getAsPredicate)
        .reduce(Predicate::and).orElse(hecho -> true);
  }

  public Map<String,String> toQueryParam() {
    Map<String,String> query = new HashMap<String,String>();
    filtros.forEach(f -> query.putAll(f.toQueryParam()));
    return query;
  }
}
