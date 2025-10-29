package ar.edu.utn.frba.dds.model.repositorios.solicitudes;

import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import java.util.Set;

public interface SolicitudDeEliminacionRepository {
  void nuevaSolicitud(SolicitudDeEliminacion solicitud);

  void aceptarSolicitud(SolicitudDeEliminacion solicitud);

  void rechazarSolicitud(SolicitudDeEliminacion solicitud);

  Set<SolicitudDeEliminacion> getPendientes();

  Set<SolicitudDeEliminacion> getAceptadas();

  Set<RechazosDeEliminacion> getRechazadas();

  Integer getRechazos(String tituloHecho);

  boolean estaEliminado(String tituloHecho);

  Set<String> hechosEliminados();
}
