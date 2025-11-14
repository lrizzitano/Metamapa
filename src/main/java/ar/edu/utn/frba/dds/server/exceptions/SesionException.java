package ar.edu.utn.frba.dds.server.exceptions;

public class SesionException extends RuntimeException {
  public SesionException(String message, Exception cause) {
    super(message, cause);
  }
}
