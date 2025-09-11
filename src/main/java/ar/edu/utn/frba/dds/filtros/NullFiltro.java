package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Map;
import java.util.function.Predicate;

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
}
