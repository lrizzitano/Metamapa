package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import java.time.LocalDate;

public class SolicitudDeCambio {

  public Hecho hechoACambiar;
  public Hecho hechoModificado;
  public Hecho sugerencias;
  public Usuario usuario;
  public Administrador responsable;
  private final FuenteDinamica fuente = FuenteDinamica.instance();

  public SolicitudDeCambio(Hecho hechoACambiar, Hecho hechoModificado,Usuario usuario) {
    if(hechoACambiar.fechaCarga().isBefore(LocalDate.now().minusDays(7))){
      throw new SolicitudDeCambioInvalidaException("Se supero el margen de 7 dias para modificar.");
    }
    else if(!usuario.estaRegistrado()){
      throw new SolicitudDeCambioInvalidaException("El usuario que solicita el cambio no esta registrado. No tiene permisos de edicion");
    }
    else if(hechoACambiar.contribuyente() != usuario){
      throw new SolicitudDeCambioInvalidaException("El usuario que solicita el cambio no es quien subio el hecho");
    }
    this.hechoACambiar = hechoACambiar;
    this.hechoModificado = hechoModificado;
    this.sugerencias =null;
    this.responsable = null;
    this.usuario = usuario;
    fuente.nuevaSolicitudDeCambio(this);
  }

  public Hecho getHechoACambiar() {
    return hechoACambiar;
  }
  public Hecho getHechoModificado() {
    return hechoModificado;
  }

  public void aceptarCambio(Administrador administrador,Hecho sugerencias) {
    if (this.responsable != null) {
      throw new SolicitudYaResueltaException();
    }
    if(sugerencias != null){ this.sugerencias =sugerencias; }

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
