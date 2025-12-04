package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.Set;
import java.util.function.Predicate;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("mock")
public class FuenteMock extends Fuente {

  @ManyToMany(cascade = CascadeType.PERSIST)
  private Set<Hecho> hechos;

  public FuenteMock(Set<Hecho> hechos) {
    this.hechos = hechos;
  }

  public FuenteMock() {

  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return hechos.stream().filter(filtro.getAsPredicate()).collect(java.util.stream.Collectors.toSet());
  }

  @Override
  public String getNombre(){
    return "Mock";
  }
}
