package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import java.time.LocalDate;

public class UsuarioDTO {
  public String nombre;
  public String apellido;
  public String usuario;
  public String fechaDeNacimiento;
  public String edad;
  public String fechaDeNacimientoSinFormatear;

  public UsuarioDTO(Usuario usuario) {
    this.nombre = usuario.getNombre();
    this.apellido = usuario.getApellido();
    this.usuario = usuario.getUsuario();
    this.fechaDeNacimiento = usuario.getFechaDeNacimiento();
    this.fechaDeNacimientoSinFormatear = usuario.getFechaNacimientoSinFormatear().toString();
  }

  public String nombre() {
    return nombre;
  }
  public String apellido() {
    return apellido;
  }
  public String usuario() {
    return usuario;
  }
  public String fechaDeNacimiento() {
    return fechaDeNacimiento;
  }
  public String fechaDeNacimientoSinFormatear() {return fechaDeNacimientoSinFormatear;}
}
