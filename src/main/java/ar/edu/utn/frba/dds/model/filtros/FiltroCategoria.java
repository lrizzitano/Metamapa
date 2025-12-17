package ar.edu.utn.frba.dds.model.filtros;


import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

@Entity
@DiscriminatorValue("categoria")
public class FiltroCategoria extends Filtro {

  @Column(name ="nombre_categoria")
  private String categoria;

  public FiltroCategoria() {}
  public FiltroCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.categoria().equalsIgnoreCase(categoria);
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of("categoria", categoria);
  }

  @Override
  public javax.persistence.criteria.Predicate toJpaPredicate(Root<Hecho> root, CriteriaBuilder cb) {
    return cb.equal(root.get("categoria"), categoria);
  }

  @Override
  public String getNombre() {
    return " Categoria: " + categoria;
  }
}