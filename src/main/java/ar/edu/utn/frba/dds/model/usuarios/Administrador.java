package ar.edu.utn.frba.dds.usuarios;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
@DiscriminatorValue("administrador")
public class Administrador extends Usuario {

  public Administrador() {}
  public Administrador(String nombre, String apellido, int edad) {
    super(requireNonNull(nombre), requireNonNull(apellido), edad);
  }
}
