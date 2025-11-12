package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.exceptions.FaltaAtributoDeUsuarioException;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioExistenteException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UsuarioController implements WithSimplePersistenceUnit {

  RepoUsuarios repoUsuarios;

  public UsuarioController() {
    repoUsuarios = new RepoUsuarios();
  }

  public void registrarUsuario(Context ctx) {
    var username = ctx.formParam("nombreDeUsuario");
    var password = ctx.formParam("password");

    if (password == null || password.isBlank() || username == null || username.isBlank()) {
      ctx.result("Usuario o contraseña incompletos");
      return;
    }

    var fechaDeNacimiento = ctx.formParam("fechaDeNacimiento");
    //Date.parse no anda en variables, qué lenguaje eh.

    String finalFechaDeNacimiento = (fechaDeNacimiento == null || fechaDeNacimiento.isBlank()) ? null : fechaDeNacimiento;
    Usuario usuario = new Usuario(
        username,
        ctx.formParam("nombre"),
        ctx.formParam("apellido"),
        finalFechaDeNacimiento == null ? null : LocalDate.parse(fechaDeNacimiento),
        password
    );
    withTransaction(() -> {
      if (repoUsuarios.findByUsername(username) != null) {
        throw new UsuarioExistenteException("Ya existe un usuario con ese nombre");
      }
      repoUsuarios.save(usuario);
    });

    ctx.appData(AppKeys.AUTENTICADOR).crearSesion(usuario, ctx);
    Usuario usuarioPersistido = repoUsuarios.findByUsername(username);
    UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioPersistido);
    ctx.sessionAttribute("usuario", usuarioDTO);
    ctx.header("HX-Redirect", "/");
  }

  public void iniciarSesion(Context ctx) {

    Usuario usuario = repoUsuarios.findByUsername(ctx.formParam("nombreDeUsuario"));
    if (usuario == null || !usuario.getPassword().equals(ctx.formParam("password"))) {
      ctx.result("Usuario o contraseña incorrectos");
    } else {
      ctx.appData(AppKeys.AUTENTICADOR).crearSesion(usuario, ctx);
      UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
      ctx.sessionAttribute("usuario", usuarioDTO);
      ctx.header("HX-Redirect", "/");
    }

  }
}
