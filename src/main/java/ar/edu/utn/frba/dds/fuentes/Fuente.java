package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.Set;

public interface Fuente {
  Set<Hecho> obtenerHechos(Filtro filtro);
}
