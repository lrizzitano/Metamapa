package ar.edu.utn.frba.dds.filtros;


import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;
import java.util.function.Predicate;
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
    return hecho -> hecho.categoria().equals(categoria);
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of("categoria", categoria);
  }
}