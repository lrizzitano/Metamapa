package ar.edu.utn.frba.dds;

import java.util.Set;
import java.util.function.Predicate;

public interface Fuente {
  Set<Hecho> obtenerHechos(Predicate<Hecho> filtro);
}
