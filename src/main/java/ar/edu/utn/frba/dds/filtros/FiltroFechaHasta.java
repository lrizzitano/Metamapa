package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDate;
import java.util.function.Predicate;

public class FiltroFechaHasta implements Filtro {
  private final LocalDate fecha;

  public FiltroFechaHasta(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.fechaAcontecimiento().isBefore(fecha);
  }

  public String toQueryParam(String prefix, String delimiter, String suffix) {
    return prefix + "fecha_acontecimiento_hasta=" + fecha.toString() + suffix;
  }
}
