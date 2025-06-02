package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.execpciones.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.hechos.Hecho;

public class SolicitudDeEliminacion {
  private final Hecho hecho;
  private final String fundamento;
  private final SolicitudesDeEliminacion solicitudes = SolicitudesDeEliminacion.instance();
  private Administrador responsable;

  public SolicitudDeEliminacion(Hecho hecho, String fundamento) {
    if (hecho == null) {
      throw new SolicitudInvalidaException("Falta hecho");
    }
    if (fundamento == null) {
      throw new SolicitudInvalidaException("Falta fundamento");
    }
    this.hecho = hecho;
    this.fundamento = fundamento;
    solicitudes.nuevaSolicitud(this);
  }

  public Hecho getHecho() {
    return hecho;
  }

  public String getFundamento() {
    return fundamento;
  }

  public Administrador getResponsable() {
    return responsable;
  }

  public void aceptar(Administrador admin) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    this.responsable = admin;
    solicitudes.aceptarSolicitud(this);
  }

  public void rechazar(Administrador admin) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    this.responsable = admin;
    solicitudes.rechazarSolicitud(this);
  }

}
