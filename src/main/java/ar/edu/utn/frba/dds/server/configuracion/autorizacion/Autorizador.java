package ar.edu.utn.frba.dds.server.configuracion.autorizacion;

import ar.edu.utn.frba.dds.server.exceptions.UsuarioNoAutorizadoException;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;

import static ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol.*;

public class Autorizador {

  public static void validarPermisos(Context ctx) {
    if(!tieneElRolMinimo(ctx.routeRoles().iterator().next(), ctx.attribute("rol"))) { // el autenticador define los roles en app.before
      throw new UsuarioNoAutorizadoException("El usuario no cuenta con los permisos para acceder a esa ruta");
    }
  }

  private static boolean tieneElRolMinimo(RouteRole requerido, Rol rolUsuario) {
    return switch (rolUsuario) {
      case ADMIN -> requerido == ADMIN || requerido == USUARIO;
      case USUARIO -> requerido == USUARIO;
      case METAMAPA -> requerido == METAMAPA;
    };
  }
}
