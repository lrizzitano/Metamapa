package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> true;
  }
}
