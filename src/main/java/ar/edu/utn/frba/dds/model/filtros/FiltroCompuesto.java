package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

@Entity
@DiscriminatorValue("compuesto")
public class FiltroCompuesto extends Filtro {

  @ManyToMany(cascade = CascadeType.ALL)
  private List<Filtro> filtros;

  public FiltroCompuesto() {
    filtros = new ArrayList<>();
  }

  public FiltroCompuesto(List<Filtro> filtros) {
    this.filtros = new ArrayList<>(filtros);
  }

  public FiltroCompuesto and(Filtro filtro) {
    this.filtros.add(filtro);
    return this;
  }

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return filtros.stream().map(Filtro::getAsPredicate)
        .reduce(Predicate::and).orElse(hecho -> true);
  }

  public Map<String, String> toQueryParam() {
    Map<String, String> query = new HashMap<>();
    filtros.forEach(f -> query.putAll(f.toQueryParam()));
    return query;
  }

  @Override
  public javax.persistence.criteria.Predicate toJpaPredicate(Root<Hecho> root, CriteriaBuilder cb) {
    return cb.and(
        filtros.stream()
            .map(f -> f.toJpaPredicate(root, cb))
            .toArray(javax.persistence.criteria.Predicate[]::new)
    );
  }

  @Override
  public String getNombre() {
    return filtros.stream()
        .map(Filtro::getNombre)
        .collect(Collectors.joining(" $ "));
  }

  public List<Filtro> filtros() {
    return filtros;
  }
}
