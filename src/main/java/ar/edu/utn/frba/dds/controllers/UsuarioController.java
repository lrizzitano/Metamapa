package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import ar.edu.utn.frba.dds.server.configuracion.autenticacion.Autenticador;
import ar.edu.utn.frba.dds.server.configuracion.autenticacion.FactoryAutenticador;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioException;
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
        ctx.result("Ya existe un usuario con ese nombre");
        return;
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

  public void cerrarSesion(Context ctx) {
    Autenticador autenticador = new FactoryAutenticador().crearAutenticador();
    ctx.cookie(autenticador.crearCookie("",0));
    ctx.sessionAttribute("usuario", null);
    ctx.redirect("/");

  }

  public Map<String, Object> usuario(Context ctx){
    Map<String, Object> model = new HashMap<>();
    String username = ctx.pathParam("username");
    Usuario usuario = repoUsuarios.findByUsername(username);
    UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
    model.put("usuario", usuarioDTO);

    return model;
  }

  public void editarPerfil(Context ctx){

    ctx.appData(AppKeys.AUTENTICADOR).validarUsuario(ctx.pathParam("username"), ctx);

    String nombre = ctx.formParam("nombre");
    String apellido = ctx.formParam("apellido");
    String nacimiento = ctx.formParam("nacimiento");

    if( nombre.isBlank() && apellido.isBlank() && nacimiento.isBlank()) {
      new Logger().info("Faltan campos por completar");
      ctx.result("Campos incompletos");
      return;
    }

    String username = ctx.pathParam("username");
    Usuario usuario = repoUsuarios.findByUsername(username);

    if(nombre!=null && !nombre.isBlank())
    {
      usuario.setNombre(nombre);
    }
    if(apellido!=null && !apellido.isBlank())
    {
      usuario.setApellido(apellido);
    }
    if(nacimiento!=null && !nacimiento.isBlank())
    {
      LocalDate fechaDeNacimiento = LocalDate.parse(nacimiento);
      usuario.setNacimiento(fechaDeNacimiento);
    }

    RepoUsuarios repoUsuarios = new RepoUsuarios();
    withTransaction(() -> {
      repoUsuarios.update(usuario);
    });

    UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
    new Logger().info("queso" + usuarioDTO.nombre());
    ctx.sessionAttribute("usuario", usuarioDTO);

    ctx.header("HX-Redirect", "/");
  }
}
