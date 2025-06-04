package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import java.time.LocalDate;

public class SolicitudDeCambio {

  private final Hecho hechoParacambiar;
  private final Hecho hechoModificado;
  private  Hecho sugerencias;
  private final Usuario usuario;
  private Administrador responsable;
  private final FuenteDinamica fuente = FuenteDinamica.instance();

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

  public void aceptarCambio(Administrador administrador, Hecho sugerencias) {
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
}
