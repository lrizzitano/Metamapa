package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.execpciones.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.model.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SolicitudDeEliminacion")
public class SolicitudDeEliminacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "solicitudDeEliminacion_id")
  private long id;

  @Column
  private String tituloHecho;

  @Column(name = "fundamento")
  private String fundamento;

  @Column
  private boolean fueAceptada;

  @ManyToOne()
  @JoinColumn(name = "administrador_id")
  private Administrador responsable;

  @Transient
  public SolicitudesDeEliminacionJPA solicitudes;

  public SolicitudDeEliminacion() {}
  public SolicitudDeEliminacion(String tituloHecho, String fundamento) {
    if (tituloHecho == null) {
      throw new SolicitudInvalidaException("Falta hecho");
    }
    if (fundamento == null) {
      throw new SolicitudInvalidaException("Falta fundamento");
    }
    this.tituloHecho = tituloHecho;
    this.fundamento = fundamento;
    this.fueAceptada = false;
    this.solicitudes = new SolicitudesDeEliminacionJPA();
  }

  public String getTituloHecho() {
    return tituloHecho;
  }

  public String getFundamento() {
    return fundamento;
  }

  public Administrador getResponsable() {
    return responsable;
  }

  public void setSolicitudes(SolicitudesDeEliminacionJPA solicitudes) {
    this.solicitudes = solicitudes;
  }

  public void aceptar(Administrador admin) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    this.responsable = admin;
    this.fueAceptada = true;


    // TODO: no se 
    solicitudes.aceptarSolicitud(this);
  }

  public void rechazar(Administrador admin) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    this.responsable = admin;
    this.fueAceptada = false;
    solicitudes.rechazarSolicitud(this);
  }

  public Long getId() {
    return id;
  }

}
