package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.usuarios.Usuario;

public class RepoUsuarios extends RepoGenerico<Usuario> {
  public RepoUsuarios() {
    super(Usuario.class);
  }
}
