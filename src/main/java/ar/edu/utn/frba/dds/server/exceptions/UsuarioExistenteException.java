package ar.edu.utn.frba.dds.server.exceptions;

public class UsuarioExistenteException extends RuntimeException {
  public UsuarioExistenteException(String message) {
    super(message);
  }
}
