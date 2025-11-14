package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.server.configuracion.autenticacion.Autenticador;
import io.javalin.config.Key;

public class AppKeys {
  public static final Key<Logger> LOGGER = new Key<>("logger");
  public static final Key<Autenticador> AUTENTICADOR = new Key<>("autenticador");
  public static final String ROL = "rol";
  public static final String AUTENTICADO = "autenticado";
  public static final String ESADMIN = "esAdmin";
  public static final String MODEL = "model";
  public static final String USUARIO = "usuario";
  public static final String TOKEN = "token";
}