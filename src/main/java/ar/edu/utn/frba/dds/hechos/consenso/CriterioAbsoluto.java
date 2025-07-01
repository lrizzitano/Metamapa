package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CriterioAbsoluto extends CriterioConsenso{

  @Override
  public Set<Hecho> actualizarHechos(){
    Set<Fuente> fuentes = this.fuentesRepository.getFuentes();

    Filtro nullFiltro = new NullFiltro();

    Set<Hecho> hechosConsensuadosNuevos = null;

    for (Fuente f : fuentes) {
      Set<Hecho> hechos = f.obtenerHechos(nullFiltro);

      if (hechosConsensuadosNuevos == null) {
        // Primera fuente: se inicializa con sus hechos
        hechosConsensuadosNuevos = new HashSet<>(hechos);
      } else {
        // Intersección progresiva
        hechosConsensuadosNuevos.retainAll(hechos); //VA DEJANDO LA INTERSECCION
      }

      if (hechosConsensuadosNuevos.isEmpty()) break;
    }

    if (hechosConsensuadosNuevos == null || hechosConsensuadosNuevos.isEmpty()) {
      hechosConsensuadosNuevos = Collections.emptySet();
    }

    return hechosConsensuadosNuevos;
  }


}
