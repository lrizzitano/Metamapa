package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.execpciones.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.SolicitudesDeEliminacion;
import ar.edu.utn.frba.dds.usuarios.Administrador;

import javax.persistence.*;

@Entity
@Table(name = "SolicitudDeEliminacion")
public class SolicitudDeEliminacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "solicitudDeEliminacion_id")
  private long id;
  @ManyToOne
  @JoinColumn(name = "hecho_id")
  private Hecho hecho;
  @Column(name = "fundamento")
  private String fundamento;
  @Transient
  private SolicitudesDeEliminacion solicitudes = SolicitudesDeEliminacion.instance();
  @ManyToOne()
  @JoinColumn(name = "administrador_id")
  private Administrador responsable;

  public SolicitudDeEliminacion() {}
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
