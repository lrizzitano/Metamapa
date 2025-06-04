package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

public class Solicitud {
  String justificacion;
  boolean esSpam;

  Solicitud(String justificacion, boolean esSpam) {
    this.justificacion = justificacion;
    this.esSpam = esSpam;
  }
}
