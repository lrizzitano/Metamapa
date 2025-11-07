package ar.edu.utn.frba.dds.server.exceptions;

public class CrearSesionException extends RuntimeException {
  public CrearSesionException(String message, Exception cause) {
    super(message, cause);
  }
}
