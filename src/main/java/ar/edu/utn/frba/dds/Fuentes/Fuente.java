package ar.edu.utn.frba.dds.Fuentes;

import ar.edu.utn.frba.dds.Hechos.Hecho;

import java.util.Set;
import java.util.function.Predicate;

public interface Fuente {
  Set<Hecho> obtenerHechos(Predicate<Hecho> filtro);
}
