package ar.edu.utn.frba.dds.model.execpciones;

public class SolicitudDeCambioInvalidaException extends RuntimeException {
  public SolicitudDeCambioInvalidaException(String message) {
    super("No se pudo generar la solicitud de cambio porque: " + message);
  }
}
