package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

public class VectorEtiquetado {
  double[] vector;
  Categoria categoria;

  VectorEtiquetado(double[] vector, Categoria categoria) {
    this.vector = vector;
    this.categoria = categoria;
  }
}
