package ar.edu.utn.frba.dds.Fuentes;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Solicitudes.Solicitud;
import ar.edu.utn.frba.dds.Solicitudes.SolicitudDeCambio;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class FuenteDinamica implements Fuente{

  private FuenteDinamica() {}

  private static final FuenteDinamica instance = new FuenteDinamica();
  private Set<Hecho> hechos;

  private final Set<SolicitudDeCambio> pendientes = new HashSet<>();
  private final Set<SolicitudDeCambio> aceptadas = new HashSet<>();
  private final Map<Hecho, Integer> rechazadas = new HashMap<>();

  public static FuenteDinamica instance() {
    return instance;
  }

  public void nuevaSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.add(solicitudDeCambio);
  }

  public void aceptarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.eliminarHecho(solicitudDeCambio.getHechoACambiar());
    this.agregarHecho(solicitudDeCambio.getHechoModificado());
    this.pendientes.remove(solicitudDeCambio);
    this.aceptadas.add(solicitudDeCambio);
  }

  public void rechazarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.remove(solicitudDeCambio);
    Hecho hecho = solicitudDeCambio.getHechoACambiar();
    rechazadas.put(hecho, rechazadas.getOrDefault(hecho, 0) + 1);
  }

  public Set<Hecho> obtenerHechos(Predicate<Hecho> filtro){
    Map<String, Hecho> hechosPorTitulo = new HashMap<>();
    for (Hecho hecho : hechos) {
      if (filtro.test(hecho)) {
        hechosPorTitulo.put(hecho.titulo(), hecho);
      }
    }
    return new HashSet<>(hechosPorTitulo.values());
  }

  public void agregarHecho(Hecho hecho)
  {
    this.hechos.add(hecho);
  }

  public void eliminarHecho(Hecho hecho)
  {
    if(hechos.contains(hecho)){

    this.hechos.remove(hecho);
    }
  }
  /*
  public void agregarHecho(Hecho hecho, Usuario usuario){
    usuario.setContribuyente(true);
    hecho.setContribuyente(usuario);
    this.hechos.add(hecho);
  }
   */
}
