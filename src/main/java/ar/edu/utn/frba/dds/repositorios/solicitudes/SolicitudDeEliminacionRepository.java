package ar.edu.utn.frba.dds.repositorios.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import java.util.Map;
import java.util.Set;

public interface SolicitudDeEliminacionRepository {
  void nuevaSolicitud(SolicitudDeEliminacion solicitud);

  void aceptarSolicitud(SolicitudDeEliminacion solicitud);

  void rechazarSolicitud(SolicitudDeEliminacion solicitud);

  Set<SolicitudDeEliminacion> getPendientes();

  Set<SolicitudDeEliminacion> getAceptadas();

  Map<String, Integer> getRechazadas();

  Integer getRechazos(String tituloHecho);

  boolean estaEliminado(String tituloHecho);

  Set<String> hechosEliminados();
}
