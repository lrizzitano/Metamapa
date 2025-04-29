package ar.edu.utn.frba.dds;

import java.util.Set;

public interface Fuente {
  Set<Hecho> obtenerHechos(Filtro criterio);
}
