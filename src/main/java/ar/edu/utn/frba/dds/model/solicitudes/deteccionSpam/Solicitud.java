package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

public class Solicitud {
  String justificacion;
  Categoria etiqueta;

  Solicitud(String justificacion, Categoria etiqueta) {
    this.justificacion = justificacion;
    this.etiqueta = etiqueta;
  }
}
