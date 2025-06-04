package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

import static java.lang.Math.sqrt;

import java.util.List;

public class ClasificadorKNN {
  List<VectorEtiquetado> vectoresDeEntrenamiento;
  int cantidadDeVecinos;

  ClasificadorKNN(List<VectorEtiquetado> vectoresDeEntrenamiento, int cantidadDeVecinos) {
      this.vectoresDeEntrenamiento = vectoresDeEntrenamiento;
      this.cantidadDeVecinos = cantidadDeVecinos;
  }

  // se asume que vector1.length = vector2.length y calcula la distancia Euclidea
  private double distanciaEntreVectores(double[] vector1, double[] vector2) {
    double sumatoriaDiferenciasAlCuadrado = 0;
    double diferencia;
    for (int i = 0; i < vector1.length; i++) {
      diferencia = vector1[i] - vector2[i];
      sumatoriaDiferenciasAlCuadrado += diferencia * diferencia;
    }
    return sqrt(sumatoriaDiferenciasAlCuadrado);
  }
}
