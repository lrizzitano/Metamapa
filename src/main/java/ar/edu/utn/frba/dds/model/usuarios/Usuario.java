package ar.edu.utn.frba.dds.model.usuarios;

import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoUsuario")
@DiscriminatorValue("usuario")
public class Usuario {

  @Id
  private String usuario;
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "apellido")
  private String apellido;
  @Column(name = "edad")
  private LocalDate fechaNacimiento;
  @Column(name = "esContribuyente")
  private boolean esContribuyente = false;
  @Column(name = "password")
  private String password;

  public Usuario() {}
  public Usuario(String usuario, String nombre, String apellido, LocalDate fechaNacimiento, String password) {
    this.usuario = usuario;
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.password = password;
  }

  public String getUsuario() {
    return usuario;
  }

  public String getFechaDeNacimiento() {
    return fechaNacimiento == null ? "Sin fecha"
        : DateTimeFormatter.ofPattern("dd/MM/yy").format(fechaNacimiento);
  }

  public LocalDate getFechaNacimientoSinFormatear() {
    return fechaNacimiento;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public void setNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public LocalDate getEdad() {
    return fechaNacimiento;
  }

  public String getPassword() {
    return password;
  }

  public boolean esContribuyente() {
    return esContribuyente;
  }

  public void contribuir() {
    this.esContribuyente = true;
  }

  public boolean estaRegistrado() {
    return nombre != null;
  }
}
