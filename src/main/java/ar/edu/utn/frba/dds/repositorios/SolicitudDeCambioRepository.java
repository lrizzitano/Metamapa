package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import java.util.Map;
import java.util.Set;

public interface SolicitudDeCambioRepository {
  void nuevaSolicitud(SolicitudDeCambio solicitudDeCambio);
  void aceptarSolicitud(SolicitudDeCambio solicitudDeCambio);
  void rechazarSolicitud(SolicitudDeCambio solicitudDeCambio);
  Set<SolicitudDeCambio> getPendientes();
  Set<SolicitudDeCambio> getAceptadas();
  Map<Hecho, Integer> getRechazadas();
}
