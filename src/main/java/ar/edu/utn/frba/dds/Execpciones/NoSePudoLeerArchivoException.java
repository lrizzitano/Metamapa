package ar.edu.utn.frba.dds.Execpciones;

public class NoSePudoLeerArchivoException extends RuntimeException {
  public NoSePudoLeerArchivoException(String message) {
    super(message);
  }
}
