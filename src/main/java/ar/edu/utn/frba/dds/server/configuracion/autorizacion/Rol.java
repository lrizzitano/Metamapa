package ar.edu.utn.frba.dds.server.configuracion.autorizacion;

import io.javalin.security.RouteRole;

public enum Rol implements RouteRole {
  USUARIO,
  ADMINISTRADOR,
  METAMAPA;
}
