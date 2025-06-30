package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.*;
import java.util.stream.Collectors;

public class CriterioMultiplesMenciones extends CriterioConsenso{

  @Override
  public void actualizar(){
    Set<Fuente> fuentes = this.fuentesRepository.getFuentes();

    Filtro nullFiltro = new NullFiltro();

    Map<String, List<Hecho>> hechosConsensuadosNuevos = new HashMap<>(); // <nombre, hechos>

    for (Fuente f : fuentes) {
      Set<Hecho> hechos = f.obtenerHechos(nullFiltro);

      for (Hecho h : hechos) {
        // Si no existe el hecho en el hashmap lo agrega
        // Si ya aparecio por otra fuente, le suma uno a las apariciones
        // aguante la tabla de hash
          hechosConsensuadosNuevos
              .compute(h.titulo(),
                  (titulo, hechosEsteTitulo) -> {
                    if (hechosEsteTitulo == null) {
                      List<Hecho> nuevaLista = new ArrayList<>();
                      nuevaLista.add(h);
                      return nuevaLista;
                    } else {
                      hechosEsteTitulo.add(h);
                      return hechosEsteTitulo;
                    }
                  });
      }
    }

    this.hechosConsensuados = hechosConsensuadosNuevos.values().stream()
        .filter(hechos -> hechos.size() > 1 && this.compartenAtributos(hechos))
        .map(lista -> lista.get(0))
        .collect(Collectors.toSet());
  }

  private boolean compartenAtributos(List<Hecho> hechos) {
    return hechos.stream()
        .map(Hecho::titulo)
        .distinct()
        .count() <= 1;
  }
}
