package ar.edu.utn.frba.dds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solicitudes {
  private static final Solicitudes instance = new Solicitudes();

  private final Set<Solicitud> pendientes = new HashSet<>();
  private final Set<Solicitud> aceptadas = new HashSet<>();
  private final Map<Hecho, Integer> rechazadas = new HashMap<>();
  private DetectorDeSpam detectorDeSpam;

  private Solicitudes() {
  }

  public static Solicitudes instance() {
    return instance;
  }

  public void setDetectorDeSpam(DetectorDeSpam detectorDeSpam) {
    this.detectorDeSpam = detectorDeSpam;
  }

  public void nuevaSolicitud(Solicitud solicitud) {
    if (detectorDeSpam != null && detectorDeSpam.esSpam(solicitud.getFundamento())) {
      this.rechazarSolicitud(solicitud);
      return;
    }
    pendientes.add(solicitud);
  }

  public void aceptarSolicitud(Solicitud solicitud) {
    pendientes.remove(solicitud);
    aceptadas.add(solicitud);
  }

  public void rechazarSolicitud(Solicitud solicitud) {
    pendientes.remove(solicitud);
    Hecho hecho = solicitud.getHecho();
    rechazadas.put(hecho, rechazadas.getOrDefault(hecho, 0) + 1);
  }

  public Set<Solicitud> getPendientes() {
    return new HashSet<>(pendientes);
  }

  public Set<Solicitud> getAceptadas() {
    return new HashSet<>(aceptadas);
  }

  public Map<Hecho, Integer> getRechazadas() {
    return new HashMap<>(rechazadas);
  }

  public Integer getRechazos(Hecho hecho) {
    return rechazadas.get(hecho);
  }

  public boolean estaEliminado(Hecho hecho) {
    return this.hechosEliminados().contains(hecho);
  }

  public Set<Hecho> hechosEliminados() {
    return new HashSet<>(aceptadas.stream().map(Solicitud::getHecho).toList());
  }

  void reset() {
    pendientes.clear();
    aceptadas.clear();
    rechazadas.clear();
  }

}