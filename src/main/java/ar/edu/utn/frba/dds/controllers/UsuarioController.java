package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.exceptions.FaltaAtributoDeUsuarioException;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioExistenteException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDate;

public class UsuarioController implements WithSimplePersistenceUnit {

  RepoUsuarios repoUsuarios;

  public UsuarioController() {
    repoUsuarios = new RepoUsuarios();
  }

  public void registrarUsuario(Context ctx) {
    var username = ctx.formParam("nombreDeUsuario");
    if (username == null || username.isBlank()) {
      throw new FaltaAtributoDeUsuarioException("Falta el nombre de usuario");
    }

    var password = ctx.formParam("password");
    if(password == null || password.isBlank()) {
      throw new FaltaAtributoDeUsuarioException("Falta el password de usuario");
    }

    var fechaDeNacimiento = ctx.formParam("fechaNacimiento");
    //Date.parse no anda en variables, qué lenguaje eh.

    String finalFechaDeNacimiento = (fechaDeNacimiento == null || fechaDeNacimiento.isBlank()) ? null : fechaDeNacimiento;
    Usuario usuario = new Usuario(
        username,
        ctx.formParam("nombre"),
        ctx.formParam("apellido"),
        finalFechaDeNacimiento == null ? null : LocalDate.parse(finalFechaDeNacimiento),
        password
    );
    withTransaction(() -> {
      if(repoUsuarios.findByUsername(username) != null) {
        throw new UsuarioExistenteException("Ya existe un usuario con ese nombre");
      }
      repoUsuarios.save(usuario);
    });

    ctx.appData(AppKeys.AUTENTICADOR).crearSesion(usuario, ctx);
  }

  public void iniciarSesion(Context ctx) {

    if(ctx.formParam("password") == null){
      throw new FaltaAtributoDeUsuarioException("Falta el password de usuario");
    }

    if(ctx.formParam("nombreDeUsuario") == null) {
      throw new FaltaAtributoDeUsuarioException("Falta el nombre de usuario");
    }

    Usuario usuario = repoUsuarios.findByUsername(ctx.formParam("nombreDeUsuario"));
    if(usuario == null) {
      throw new UsuarioExistenteException("No existe un usuario con ese nombre");
    }

    if(!usuario.getPassword().equals(ctx.formParam("password"))) {
      throw new UsuarioExistenteException("Password incorrecta");
    }

    ctx.appData(AppKeys.AUTENTICADOR).crearSesion(usuario, ctx);
  }
}
