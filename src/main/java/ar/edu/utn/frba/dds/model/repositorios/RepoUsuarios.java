package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;

import java.util.List;

public class RepoUsuarios extends RepoGenerico<Usuario> {
  public RepoUsuarios() {
    super(Usuario.class);
  }

  public List<Usuario> findByNombre(String nombre) {
    return entityManager().createQuery(
            "select u " +
                "from Usuario u " +
                "where u.nombre = :nombre",
            Usuario.class
        )
        .setParameter("nombre", nombre)
        .getResultList();
  }
}
