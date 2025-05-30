package ar.edu.utn.frba.dds.execpciones;

public class NoSePudoLeerArchivoException extends RuntimeException {
  public NoSePudoLeerArchivoException(String message) {
    super(message);
  }
}
