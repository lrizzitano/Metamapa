package ar.edu.utn.frba.dds.server.configuracion.autorizacion;

import ar.edu.utn.frba.dds.server.configuracion.AppKeys;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioNoAutorizadoException;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;

import static ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol.*;

public class Autorizador {

  public static void validarPermisosDeRuta(Context ctx) {

    String path = ctx.path();
    if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
      return; // no controlar archivos estáticos
    }

    if (ctx.routeRoles().isEmpty()) {
      return;
    }

    if(!tieneElRolMinimo(ctx.routeRoles().iterator().next(), ctx.attribute(AppKeys.ROL))) { // el autenticador define los roles en app.before
      throw new UsuarioNoAutorizadoException("El usuario no cuenta con los permisos para acceder a esa ruta");
    }
  }

  private static boolean tieneElRolMinimo(RouteRole requerido, Rol rolUsuario) {
    return switch (rolUsuario) {
      case ADMINISTRADOR -> requerido == ADMINISTRADOR || requerido == USUARIO;
      case USUARIO -> requerido == USUARIO;
      case METAMAPA -> requerido == METAMAPA;
    };
  }
}
