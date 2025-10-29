package ar.edu.utn.frba.dds.model.execpciones;

public class NoSePudoLeerArchivoException extends RuntimeException {
  public NoSePudoLeerArchivoException(String message) {
    super(message);
  }
}
