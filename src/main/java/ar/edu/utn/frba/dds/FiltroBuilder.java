package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FiltroBuilder {
  private final List<Predicate<Hecho>> filtros = new ArrayList<>();

  public FiltroBuilder() {
  }

  public FiltroBuilder añadirFiltro(Predicate<Hecho> filtro) {
    filtros.add(filtro);
    return this;
  }

  public Predicate<Hecho> obtenerFiltroAnd() {
    return filtros.stream().reduce(Predicate::and).orElse(hecho -> true);
  }

  public Predicate<Hecho> obtenerFiltroOr() {
    return filtros.stream().reduce(Predicate::or).orElse(hecho -> true);
  }
}