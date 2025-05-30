package ar.edu.utn.frba.dds.Solicitudes;

import ar.edu.utn.frba.dds.Execpciones.HechoInvalidoException;
import ar.edu.utn.frba.dds.Execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.Execpciones.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.Execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.Fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Usuarios.Administrador;
import ar.edu.utn.frba.dds.Usuarios.Usuario;
import java.time.LocalDate;

public class SolicitudDeCambio {
  public Hecho hechoACambiar;
  public Hecho hechoModificado;
  public Hecho sugerencias;
  public Usuario usuario;
  public Administrador responsable;
  private final FuenteDinamica fuente = FuenteDinamica.instance();

  public SolicitudDeCambio(Hecho hechoACambiar, Hecho hechoModificado,Usuario usuario) {
    if(hechoACambiar.fechaCarga().isEqual(LocalDate.now())){
      throw new SolicitudDeCambioInvalidaException("Se supero el margen de 7 dias para modificar.");
    }
    else if(hechoACambiar.contribuyente() != usuario){
      throw new SolicitudDeCambioInvalidaException("El usuario que solicita el cambio no es quien subio el hecho");
    }
    else{
      this.hechoACambiar = hechoACambiar;
      this.hechoModificado = hechoModificado;
      this.sugerencias =null;
      this.responsable = null;
      this.usuario = usuario;
      fuente.nuevaSolicitudDeCambio(this);
    }
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
