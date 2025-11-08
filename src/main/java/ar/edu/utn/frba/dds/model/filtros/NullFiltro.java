package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

@Entity
@DiscriminatorValue("null")
public class NullFiltro extends Filtro {

  public NullFiltro() {}

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of();
  }

  @Transient
  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> true;
  }

  @Override
  public javax.persistence.criteria.Predicate toJpaPredicate(Root<Hecho> root, CriteriaBuilder cb) {
    return cb.conjunction();
  }

  @Override
  public String getNombre() {
    return " Nulo ";
  }
}
