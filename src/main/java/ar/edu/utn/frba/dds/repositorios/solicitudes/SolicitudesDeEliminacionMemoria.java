package ar.edu.utn.frba.dds.repositorios.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.NullDetector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class SolicitudesDeEliminacionMemoria implements SolicitudDeEliminacionRepository {
  private static final SolicitudesDeEliminacionMemoria instance = new SolicitudesDeEliminacionMemoria();

  private final Set<SolicitudDeEliminacion> pendientes = new HashSet<>();
  private final Set<SolicitudDeEliminacion> aceptadas = new HashSet<>();
  private final Map<Hecho, Integer> rechazadas = new HashMap<>();
  private DetectorDeSpam detectorDeSpam = new NullDetector();

  private SolicitudesDeEliminacionMemoria() {
  }

  public static SolicitudesDeEliminacionMemoria instance() {
    return instance;
  }

  public void setDetectorDeSpam(DetectorDeSpam detectorDeSpam) {
    this.detectorDeSpam = requireNonNull(detectorDeSpam);
  }

  public void nuevaSolicitud(SolicitudDeEliminacion solicitud) {
    if (detectorDeSpam.esSpam(solicitud.getFundamento())) {
      this.rechazarSolicitud(solicitud);
      return;
    }
    pendientes.add(solicitud);
  }

  public void aceptarSolicitud(SolicitudDeEliminacion solicitud) {
    pendientes.remove(solicitud);
    aceptadas.add(solicitud);
  }

  public void rechazarSolicitud(SolicitudDeEliminacion solicitud) {
    pendientes.remove(solicitud);
    Hecho hecho = solicitud.getHecho();
    rechazadas.put(hecho, rechazadas.getOrDefault(hecho, 0) + 1);
  }

  public Set<SolicitudDeEliminacion> getPendientes() {
    return new HashSet<>(pendientes);
  }

  public Set<SolicitudDeEliminacion> getAceptadas() {
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
    return new HashSet<>(aceptadas.stream().map(SolicitudDeEliminacion::getHecho).toList());
  }

  public void reset() {
    pendientes.clear();
    aceptadas.clear();
    rechazadas.clear();
  }
}