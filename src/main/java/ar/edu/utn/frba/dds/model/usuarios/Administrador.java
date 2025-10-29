package ar.edu.utn.frba.dds.model.usuarios;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static java.util.Objects.requireNonNull;

@Entity
@DiscriminatorValue("administrador")
public class Administrador extends Usuario {

  public Administrador() {}
  public Administrador(String nombre, String apellido, int edad) {
    super(requireNonNull(nombre), requireNonNull(apellido), edad);
  }
}
