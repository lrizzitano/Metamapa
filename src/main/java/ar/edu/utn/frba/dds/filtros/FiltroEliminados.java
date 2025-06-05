package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesDeEliminacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class FiltroEliminados implements Filtro {
  private final Set<Hecho> hechosEliminados;

  public FiltroEliminados(SolicitudesDeEliminacion solicitudes) {
    hechosEliminados = solicitudes.hechosEliminados();
  }

  public Predicate<Hecho> getAsPredicate() {
    return hecho -> !hechosEliminados.contains(hecho);
  }

  public Map<String,String> toQueryParam() {
    Map<String,String> query = new HashMap<String,String>();
    query.put(this.toString(), categoria);
    return query;
  }
}
