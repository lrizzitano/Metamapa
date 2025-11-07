package ar.edu.utn.frba.dds.server.exceptions;

public class UsuarioNoAutorizadoException extends RuntimeException {
  public UsuarioNoAutorizadoException(String message) {
    super(message);
  }
}
