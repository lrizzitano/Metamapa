package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

@Entity
@DiscriminatorValue("fechaHasta")
public class FiltroFechaHasta extends Filtro {

  @Column(name="fechaMaxima")
  private LocalDateTime fecha;

  public FiltroFechaHasta() {}

  public FiltroFechaHasta(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.fechaAcontecimiento().isBefore(fecha);
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of("fechaHasta", fecha.toString());
  }

  @Override
  public javax.persistence.criteria.Predicate toJpaPredicate(Root<Hecho> root, CriteriaBuilder cb) {
    return cb.lessThanOrEqualTo(root.get("fechaAcontecimiento"), fecha);

  }
}
