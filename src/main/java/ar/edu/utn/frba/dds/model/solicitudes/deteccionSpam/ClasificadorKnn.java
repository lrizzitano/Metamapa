package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

import java.util.Comparator;
import java.util.List;

import static java.lang.Math.sqrt;

public class ClasificadorKnn {
  List<VectorEtiquetado> vectoresDeEntrenamiento;
  int cantidadDeVecinos;

  ClasificadorKnn(List<VectorEtiquetado> vectoresDeEntrenamiento, int cantidadDeVecinos) {
      this.vectoresDeEntrenamiento = vectoresDeEntrenamiento;
      this.cantidadDeVecinos = cantidadDeVecinos;
  }

  boolean esDeCategoria(Categoria unaCategoria, double[] vectorAClasificar) {
     return vectoresDeEntrenamiento.stream()
        .map(v ->
            new Vecino(distanciaEntreVectores(v.vector, vectorAClasificar), v.etiqueta))
        .sorted(Comparator.comparingDouble(vecino -> vecino.distancia))
        .limit(this.cantidadDeVecinos)
        .filter(vecino -> vecino.etiqueta.equals(unaCategoria))
        .count() >= (this.cantidadDeVecinos/2 + 1);
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
