package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;

public class RepoUsuarios extends RepoGenerico<Usuario> {
  public RepoUsuarios() {
    super(Usuario.class);
  }

  public Usuario findByUsername(String username) {
    return super.find(Usuario.class, username);
  }
}
