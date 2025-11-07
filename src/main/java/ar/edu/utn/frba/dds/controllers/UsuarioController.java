package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioExistenteException;
import io.javalin.http.Context;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class UsuarioController {

  RepoUsuarios repoUsuarios;

  public UsuarioController() {
    repoUsuarios = new RepoUsuarios();
  }

  public void registrarUsuario(Context ctx) {
    if(!repoUsuarios.findAll().isEmpty()) {
      throw new UsuarioExistenteException("Ya existe un usuario con ese nombre");
    }

    repoUsuarios.save(new Usuario(
        ctx.formParam("usuario"),
        ctx.formParam("nombre").concat(ctx.formParam("apellido")),
        null //esta edad actualmente, // nos falta la contraseña
    ));
  }
}
