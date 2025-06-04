package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesDeEliminacion;
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

  public String toQueryParam(String prefix, String deliimiter, String suffix) {
    return "";
  }
}
