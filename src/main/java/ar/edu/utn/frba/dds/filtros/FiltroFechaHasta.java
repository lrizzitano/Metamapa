package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Predicate;

public class FiltroFechaHasta implements Filtro {
  private final LocalDate fecha;

  public FiltroFechaHasta(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.fechaAcontecimiento().isBefore(fecha);
  }

  public Map<String,String> toQueryParam() {
    return Map.of("fechaHasta", fecha.toString());
  }
}
