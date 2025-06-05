package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

public class Vecino {
  public double distancia;
  public Categoria etiqueta;

  Vecino(double distancia, Categoria etiqueta) {
    this.distancia = distancia;
    this.etiqueta = etiqueta;
  }
}
