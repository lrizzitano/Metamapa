package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import java.time.LocalDate;
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
@Table(name ="SolicitudDeCambio")
public class SolicitudDeCambio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne
  @JoinColumn(name = "hechoACambiar")
  private  Hecho hechoParacambiar;

  @ManyToOne
  @JoinColumn(name = "hechoModificado")
  private  Hecho hechoModificado;

  @Column(name = "sugerencias")
  private String sugerencias;

  @ManyToOne
  @JoinColumn(name = "usuario")
  private  Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "administrador")
  private  Administrador responsable;

  @Transient
  private  FuenteDinamica fuente = FuenteDinamica.instance();

  public SolicitudDeCambio(){}

  public SolicitudDeCambio(Hecho hechoParacambiar, Hecho hechoModificado, Usuario usuario) {
    if (hechoParacambiar.fechaCarga().isBefore(LocalDate.now().minusDays(7))) {
      throw new SolicitudDeCambioInvalidaException("Se supero el margen de 7 dias para modificar.");
    }
    if (!usuario.estaRegistrado()) {
      throw new SolicitudDeCambioInvalidaException(
          "El usuario que solicita el cambio no esta registrado. No tiene permisos de edicion");
    }
    if (hechoParacambiar.contribuyente() != usuario) {
      throw new SolicitudDeCambioInvalidaException(
          "El usuario que solicita el cambio no es quien subio el hecho");
    }
    this.hechoParacambiar = hechoParacambiar;
    this.hechoModificado = hechoModificado;
    this.usuario = usuario;
    fuente.nuevaSolicitudDeCambio(this);
  }

  public Hecho getHechoParacambiar() {
    return hechoParacambiar;
  }

  public Hecho getHechoModificado() {
    return hechoModificado;
  }

  public Administrador getResponsable() {
    return responsable;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void aceptarCambio(Administrador administrador, String sugerencias) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    this.sugerencias = sugerencias;

    this.responsable = administrador;
    fuente.aceptarSolicitudDeCambio(this);
  }

  public void rechazarCambio(Administrador administrador) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    this.responsable = administrador;
    fuente.rechazarSolicitudDeCambio(this);
  }

  public Long getId() {
    return id;
  }

}
