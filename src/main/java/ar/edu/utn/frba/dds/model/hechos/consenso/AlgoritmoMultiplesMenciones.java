package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("multiplesMenciones")
public class AlgoritmoMultiplesMenciones extends AlgoritmoConsenso{

  @Override
  public Set<Hecho> getHechosConsensuados(Set<Fuente> fuentes) {
    Filtro nullFiltro = new NullFiltro();

    Map<String, List<Hecho>> hechosConsensuados = new HashMap<>(); // <título, hechos>

    for (Fuente f : fuentes) {
      Set<Hecho> hechos = f.obtenerHechos(nullFiltro);

      for (Hecho h : hechos) {
        // Si no existe el hecho en el hashmap lo agrega
        // Si ya aparecio por otra fuente, le suma uno a las apariciones
        // aguante la tabla de hash
          hechosConsensuados.computeIfAbsent(h.titulo(), t -> new ArrayList<>()).add(h);
      }
    }

    return hechosConsensuados.values().stream()
        .filter(hechos -> hechos.size() > 1 && this.compartenAtributos(hechos))
        .map(lista -> lista.get(0))
        .collect(Collectors.toSet());
  }

  private boolean compartenAtributos(List<Hecho> hechos) {
    return hechos.stream().distinct().count() == 1;
  }

  @Override
  public String getNombre(){
    return "Multiples Menciones";
  }
}
