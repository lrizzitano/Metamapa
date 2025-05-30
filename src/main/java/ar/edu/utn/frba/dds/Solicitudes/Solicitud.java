package ar.edu.utn.frba.dds.Solicitudes;

import ar.edu.utn.frba.dds.Usuarios.Administrador;
import ar.edu.utn.frba.dds.Execpciones.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.Execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.Hechos.Hecho;

public class Solicitud {
  private final Hecho hecho;
  private final String fundamento;
  private final Solicitudes solicitudes = Solicitudes.instance();
  private Administrador responsable;

  public Solicitud(Hecho hecho, String fundamento) {
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
