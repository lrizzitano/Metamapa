package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("absoluto")
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

  @Override
  public String getNombre(){
    return "Absoluto";
  }
}