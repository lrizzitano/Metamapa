package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacionRepository;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesDeEliminacion;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class FiltroEliminados implements Filtro {
  private final Set<Hecho> hechosEliminados;

  public FiltroEliminados(SolicitudDeEliminacionRepository solicitudes) {
    hechosEliminados = solicitudes.hechosEliminados();
  }

  public Predicate<Hecho> getAsPredicate() {
    return hecho -> !hechosEliminados.contains(hecho);
  }

  public Map<String,String> toQueryParam() {
    return new HashMap<>();
  }
}
