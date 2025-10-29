package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

public class VectorEtiquetado {
  double[] vector;
  Categoria etiqueta;

  VectorEtiquetado(double[] vector, Categoria etiqueta) {
    this.vector = vector;
    this.etiqueta = etiqueta;
  }
}
