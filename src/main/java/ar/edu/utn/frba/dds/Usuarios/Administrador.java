package ar.edu.utn.frba.dds.Usuarios;

import static java.util.Objects.requireNonNull;

public class Administrador extends Usuario {

  public Administrador(String nombre, String apellido, int edad) {
    super(requireNonNull(nombre), requireNonNull(apellido), edad);
  }

}
