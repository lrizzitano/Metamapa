package ar.edu.utn.frba.dds.model.execpciones;

public class SolicitudInvalidaException extends RuntimeException {

  public SolicitudInvalidaException(String message) {
    super("No se creo la solicitud porque:  " + message);
  }
}
