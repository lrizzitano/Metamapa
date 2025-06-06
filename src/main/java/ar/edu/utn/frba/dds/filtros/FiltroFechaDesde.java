package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class FiltroFechaDesde implements Filtro {
  private final LocalDate fecha;

  public FiltroFechaDesde(LocalDate fecha) {
    this.fecha = fecha;
  }

  public Predicate<Hecho> getAsPredicate() {
    return hecho -> hecho.fechaAcontecimiento().isAfter(fecha);
  }

  public Map<String,String> toQueryParam() {
    Map<String,String> query = new HashMap<String,String>();
    query.put("fechaDesde", fecha.toString());
    return query;
  }
}
