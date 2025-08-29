package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AlgoritmoConsensoAbsoluto extends AlgoritmoConsenso {

  @Override
  public Set<Hecho> getHechosConsensuados(Set<Fuente> fuentes) {
    if (fuentes.isEmpty()) {
      return Collections.emptySet();
    }
    Filtro nullFiltro = new NullFiltro();
    Iterator<Fuente> fuenteIterator = fuentes.iterator();
    Set<Hecho> consensuados = new HashSet<>(fuenteIterator.next().obtenerHechos(nullFiltro));
    while (fuenteIterator.hasNext() && !consensuados.isEmpty()) {
      consensuados.retainAll(fuenteIterator.next().obtenerHechos(nullFiltro));
    }
    return consensuados;
  }
}