package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static java.lang.Math.ceil;

@Entity
@DiscriminatorValue("mayoriaSimple")
public class AlgoritmoMayoriaSimple extends AlgoritmoConsenso{

  @Override
  public Set<Hecho> getHechosConsensuados(Set<Fuente> fuentes) {
    Filtro nullFiltro = new NullFiltro();

    Map<Hecho,Integer> hechosConsensuados = new HashMap<>();

    for (Fuente f : fuentes) {
      Set<Hecho> hechos = f.obtenerHechos(nullFiltro);

      for (Hecho h : hechos) {
        // Si no existe el hecho en el hashmap lo agrega
        // Si ya aparecio por otra fuente, le suma uno a las apariciones
        // aguante la tabla de hash
        hechosConsensuados.merge(h, 1, Integer::sum);
      }

    }

    int cantidadFuentes = (int) ceil( fuentes.size() /2.0);

    // Filtramos los hechos que aparezcan en más de la mitad de las fuentes
    return hechosConsensuados.entrySet().stream()
        .filter(entrada -> entrada.getValue() >= cantidadFuentes)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  @Override
  public String getNombre(){
    return "Mayoria Simple";
  }
}
