package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

@Entity
@DiscriminatorValue("fechaDesde")
public class FiltroFechaDesde extends Filtro {

  @Column(name = "fechaMinima")
  private LocalDateTime fecha;

  public FiltroFechaDesde() {}

  public FiltroFechaDesde(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  @Override
  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.fechaAcontecimiento().isAfter(fecha)
        || hecho.fechaAcontecimiento().isEqual(fecha);
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of("fechaDesde", fecha.toString());
  }

  @Override
  public javax.persistence.criteria.Predicate toJpaPredicate(Root<Hecho> root, CriteriaBuilder cb) {
    return cb.greaterThanOrEqualTo(root.get("fechaAcontecimiento"), fecha);
  }

  @Override
  public String getNombre() {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    return " Fecha desde: " + fecha.format(formatter);
  }
}
