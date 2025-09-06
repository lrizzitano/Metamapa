package ar.edu.utn.frba.dds.repositorios.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import java.util.Map;
import java.util.Set;

public interface SolicitudDeCambioRepository {
  void nuevaSolicitud(SolicitudDeCambio solicitudDeCambio);

  void aceptarSolicitud(SolicitudDeCambio solicitudDeCambio);

  void rechazarSolicitud(SolicitudDeCambio solicitudDeCambio);

  Set<SolicitudDeCambio> getPendientes();

  Set<SolicitudDeCambio> getAceptadas();

  Map<Hecho, Integer> getRechazadas();

  Integer getRechazos(Hecho hecho);

}
