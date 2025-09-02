package ar.edu.utn.frba.dds.filtros;


import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Map;
import java.util.function.Predicate;

public class FiltroCategoria extends Filtro {
  private final String categoria;

  public FiltroCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.categoria().equals(categoria);
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of("categoria", categoria);
  }
}