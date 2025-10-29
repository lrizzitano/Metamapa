package ar.edu.utn.frba.dds.model.execpciones;

public class SolicitudYaResueltaException extends RuntimeException {
  public SolicitudYaResueltaException() {
    super("No se puede resolver la solicitud porque ya fue resuelta anteriormente");
  }
}
