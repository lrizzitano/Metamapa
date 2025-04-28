package ar.edu.utn.frba.dds;

public class SolicitudInvalidaException extends RuntimeException {

  public SolicitudInvalidaException(String message) {
    super("No se creo la solicitud porque:  " + message);
  }
}
