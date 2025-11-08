package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;

import java.util.List;

public class RepoUsuarios extends RepoGenerico<Usuario> {
  public RepoUsuarios() {
    super(Usuario.class);
  }

  public Usuario findByNombre(String nombre) {
    return entityManager().find(Usuario.class, nombre);
  }
}
