package ar.edu.utn.frba.dds.model.usuarios;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Entity
@DiscriminatorValue("administrador")
public class Administrador extends Usuario {

  public Administrador() {}
  public Administrador(String usuario, String nombre, String apellido, LocalDate fechaNacimiento, String password) {
    super(usuario, nombre, apellido, fechaNacimiento, password);
  }
}
