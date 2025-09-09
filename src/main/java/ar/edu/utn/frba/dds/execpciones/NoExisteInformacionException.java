package ar.edu.utn.frba.dds.execpciones;

public class NoExisteInformacionException extends RuntimeException {
  public NoExisteInformacionException(String message) {
    super("No se pudo realizar la estadistica por:  " + message);
  }
}
