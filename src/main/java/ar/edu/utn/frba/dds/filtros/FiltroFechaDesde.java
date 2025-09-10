package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Predicate;

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
    return hecho -> hecho.fechaAcontecimiento().isAfter(fecha);
  }

  @Override
  public Map<String, String> toQueryParam() {
    return Map.of("fechaDesde", fecha.toString());
  }
}
