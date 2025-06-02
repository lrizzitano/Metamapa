package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDate;
import java.util.function.Predicate;

public class FiltroFechaDesde implements Filtro {
  private final LocalDate fecha;

  public FiltroFechaDesde(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.fechaAcontecimiento().isAfter(fecha);
  }

  public String toHttp() {
    return "fecha_acontecimiento_desde=" + fecha.toString();
  }
}
