package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.Set;
import java.util.function.Predicate;

public interface Fuente {
  Set<Hecho> obtenerHechos(Predicate<Hecho> filtro);
}
