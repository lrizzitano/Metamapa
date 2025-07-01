package ar.edu.utn.frba.dds.hechos.consenso;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.*;
import java.util.stream.Collectors;

public class CriterioMayoriaSimple extends CriterioConsenso{

  @Override
  public Set<Hecho> actualizarHechos() {
    Set<Fuente> fuentes = this.fuentesRepository.getFuentes();

    Filtro nullFiltro = new NullFiltro();

    Map<Hecho,Integer> hechosConsensuadosNuevos = new HashMap<>();

    for (Fuente f : fuentes) {
      Set<Hecho> hechos = f.obtenerHechos(nullFiltro);

      for (Hecho h : hechos) {
        // Si no existe el hecho en el hashmap lo agrega
        // Si ya aparecio por otra fuente, le suma uno a las apariciones
        // aguante la tabla de hash
        hechosConsensuadosNuevos
            .compute(h, (clave, apariciones) -> apariciones == null ? 1 : apariciones + 1);
      }

    }

    int cantidadFuentes = this.fuentesRepository.getFuentes().size();

    // Filtramos los hechos que aparezcan en mas de la mitad de las fuentes
    return hechosConsensuadosNuevos.entrySet().stream()
        .filter(entrada -> entrada.getValue() >= cantidadFuentes/2)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }
}
