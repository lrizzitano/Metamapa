package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FiltroCompuestoBuilder {
  private List<Predicate<Hecho>> filtros = new ArrayList<>();

  public FiltroCompuestoBuilder() {
  }

  public void reset() {
    filtros.clear();
  }

  public FiltroCompuestoBuilder agregarFiltro(Predicate<Hecho> filtro) {
    filtros.add(filtro);
    return this;
  }

  public FiltroCompuestoBuilder negarFiltros() {
    filtros = filtros.stream().map(Predicate::negate).toList();
    return this;
  }

  public Predicate<Hecho> componerFiltrosAnd() {
    return filtros.stream().reduce(Predicate::and).orElse(hecho -> true);
  }

  public Predicate<Hecho> componerFiltrosOr() {
    return filtros.stream().reduce(Predicate::or).orElse(hecho -> true);
  }

}