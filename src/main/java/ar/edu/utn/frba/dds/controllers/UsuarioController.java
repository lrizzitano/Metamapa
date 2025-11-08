package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.exceptions.FaltaAtributoDeUsuarioException;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioExistenteException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UsuarioController implements WithSimplePersistenceUnit {

  RepoUsuarios repoUsuarios;

  public UsuarioController() {
    repoUsuarios = new RepoUsuarios();
  }

  public void registrarUsuario(Context ctx) {

    if(ctx.formParam("nombreDeUsuario") == null || ctx.formParam("nombreDeUsuario").isBlank()) { // juanma se reire pero sin el isBlank no funciona
      throw new FaltaAtributoDeUsuarioException("Falta el nombre de usuario");
    }

    if(ctx.formParam("password") == null || ctx.formParam("password").isBlank()) {
      throw new FaltaAtributoDeUsuarioException("Falta el password de usuario");
    }

    if(repoUsuarios.findByNombre(ctx.formParam("nombreDeUsuario")) != null) {
      throw new UsuarioExistenteException("Ya existe un usuario con ese nombre");
    }

    LocalDate fechaDeNacimiento;
    if(ctx.formParam("fechaDeNacimiento") == null) {
      fechaDeNacimiento = null;
    } else {
      fechaDeNacimiento = LocalDate.parse(ctx.formParam("fechaDeNacimiento"));
    }

    withTransaction(()-> {
      Usuario usuario = new Usuario(
          ctx.formParam("nombreDeUsuario"),
          ctx.formParam("nombre"),
          ctx.formParam("apellido"),
          fechaDeNacimiento,
          ctx.formParam("password")
      );
      repoUsuarios.save(usuario);
      ctx.appData(AppKeys.AUTENTICADOR).crearSesion(usuario, ctx);
    });

  }

  public void iniciarSesion(Context ctx) {

    ctx.appData(AppKeys.LOGGER).info("USUARIOSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
    repoUsuarios.findAll().forEach(u -> {ctx.appData(AppKeys.LOGGER).info(u.getUsuario());});

    if(ctx.formParam("password") == null){
      throw new FaltaAtributoDeUsuarioException("Falta el password de usuario");
    }
    if(ctx.formParam("nombreDeUsuario") == null) {
      throw new FaltaAtributoDeUsuarioException("Falta el nombre de usuario");
    }

    Usuario usuario = repoUsuarios.findByNombre(ctx.formParam("nombreDeUsuario"));
    if(usuario == null) {
      throw new UsuarioExistenteException("No existe un usuario con ese nombre");
    }
    if(!usuario.getPassword().equals(ctx.formParam("password"))) {
      throw new UsuarioExistenteException("Password incorrecta");
    }

    ctx.appData(AppKeys.AUTENTICADOR).crearSesion(usuario, ctx);
  }
}
