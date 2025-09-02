package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Predicate;

@Entity
@DiscriminatorValue("fechaHasta")
public class FiltroFechaHasta extends Filtro {

  @Column(name="fechaMaxima")
  private LocalDate fecha;

  public FiltroFechaHasta() {}

  public FiltroFechaHasta(LocalDate fecha) {
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
}
