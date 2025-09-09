package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.execpciones.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.usuarios.Administrador;

import javax.persistence.*;

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
