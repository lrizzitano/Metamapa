package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    return distinctByKey(
        hechos.stream(),
        h -> Objects.hash(
            h.titulo(), h.descripcion(), h.categoria(),
            h.latitud(), h.longitud(),
            h.fechaCarga(), h.fechaAcontecimiento(), h.origen()
        )
    ).count() == 1;
  }
  // crea una clave con los atributos y los compara en base a eso, es la solucion para no modificar la logica existente
  private static <T> Stream<T> distinctByKey(Stream<T> stream, Function<? super T, ?> keyExtractor) {
    return stream.collect(
        Collectors.toMap(
            keyExtractor,      // cómo obtener la clave
            e -> e,            // el valor es el mismo objeto
            (a, b) -> a        // en caso de colisión, quedate con el primero
        )
    ).values().stream();
  }
}
