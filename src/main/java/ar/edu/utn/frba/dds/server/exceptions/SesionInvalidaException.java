package ar.edu.utn.frba.dds.server.exceptions;

public class SesionInvalidaException extends RuntimeException {
  public SesionInvalidaException(String message, Exception cause) {
    super(message, cause);
  }
}
