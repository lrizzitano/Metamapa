package ar.edu.utn.frba.dds.model.execpciones;

public class HechoInvalidoException extends RuntimeException {
  public HechoInvalidoException(String message) {
    super("No se pudo crear el Hecho porque:  " + message);
  }
}
