package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.Usuarios.Usuario;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import ar.edu.utn.frba.dds.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;

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

  public Set<Hecho> obtenerHechos(Filtro filtro){
    Map<String, Hecho> hechosPorTitulo = new HashMap<>();
    Predicate<Hecho> filtro1 = filtro.getAsPredicate();
    for (Hecho hecho : hechos) {
      if (filtro1.test(hecho)) {
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
    if(!hechos.contains(hecho)) {
      throw new NoSePuedeEliminarUnHechoQueNoExisteException();
    }
    this.hechos.remove(hecho);
  }

  public void agregarHecho(Hecho hecho, Usuario usuario){
    //hecho.setContribuyente(usuario); Deberia existir, o el Hecho viene con este Usuario asignado
    usuario.contribuir();
    this.hechos.add(hecho);
  }

}
