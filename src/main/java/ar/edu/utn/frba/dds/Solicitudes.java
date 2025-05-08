package ar.edu.utn.frba.dds;

import java.util.HashSet;
import java.util.Set;

public class Solicitudes {
  private static final Solicitudes instance = new Solicitudes();

  private final Set<Solicitud> pendientes = new HashSet<>();
  private final Set<Solicitud> aceptadas = new HashSet<>();
  private final Set<Solicitud> rechazadas = new HashSet<>();

  private Solicitudes() {} // private constructor

  public static Solicitudes instance() {
    return instance;
  }

  public void nuevaSolicitud(Solicitud solicitud) {
    pendientes.add(solicitud);
  }

  public void aceptarSolicitud(Solicitud solicitud) {
    if (pendientes.remove(solicitud)) {
      aceptadas.add(solicitud);
    }
  }

  public void rechazarSolicitud(Solicitud solicitud) {
    if (pendientes.remove(solicitud)) {
      rechazadas.add(solicitud);
    }
  }

  public Set<Solicitud> getPendientes() {
    return new HashSet<>(pendientes);
  }

  public Set<Solicitud> getAceptadas() {
    return new HashSet<>(aceptadas);
  }

  public Set<Solicitud> getRechazadas() {
    return new HashSet<>(rechazadas);
  }

  public boolean estaEliminado(Hecho hecho) {
    return this.hechosEliminados().contains(hecho);
  }

  public Set<Hecho> hechosEliminados() {
    return new HashSet<>(aceptadas.stream().map(Solicitud::getHecho).toList());
  }

  void reset(){
    pendientes.clear();
    aceptadas.clear();
    rechazadas.clear();
  }

}