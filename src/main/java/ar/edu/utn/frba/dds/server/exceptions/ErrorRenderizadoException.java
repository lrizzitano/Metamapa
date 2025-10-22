package ar.edu.utn.frba.dds.server.exceptions;

public class ErrorRenderizadoException extends RuntimeException {
  public ErrorRenderizadoException(String message, Exception cause) {
    super(message, cause);
  }
}
