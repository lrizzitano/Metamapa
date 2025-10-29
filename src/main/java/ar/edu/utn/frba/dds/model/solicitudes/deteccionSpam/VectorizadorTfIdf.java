package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.log;

public class VectorizadorTfIdf {
  List<String> vocabulario = new ArrayList<>(); // usamos una lista para estandarizar el orden de las palabras
  Map<String, Double> IDFs = new HashMap<>();

  VectorizadorTfIdf(List<String> documentos) {
    // el preprocesamiento del corpus de data lo hacemos aca asi se hace al instanciar el vectorizador
    // luego al vectorizar un nuevo documento no repetimos este proceso
    Map<String, Integer> contadorAparicionesPalabra = new HashMap<>();

    documentos.forEach(documento -> procesarDocumento(documento, contadorAparicionesPalabra));
    calcularIDFsCorpus(contadorAparicionesPalabra, documentos.size());
  }

  private void procesarDocumento(String documento, Map<String, Integer> contadorAparicionesPalabra) {
    String[] palabras = this.parsearPalabras(documento);
    Set<String> palabrasYaContadas = new HashSet<>();

    Arrays.stream(palabras)
        .forEach(palabra -> {
          if (!palabrasYaContadas.contains(palabra)) {
            if (contadorAparicionesPalabra.containsKey(palabra)) {
              contadorAparicionesPalabra.put(palabra, contadorAparicionesPalabra.get(palabra) + 1);
            } else {
              vocabulario.add(palabra);
              contadorAparicionesPalabra.put(palabra, 1);
            }
          }
          palabrasYaContadas.add(palabra);
        });
  }

  private String[] parsearPalabras(String texto) {
    return texto.toLowerCase().replaceAll("\\p{Punct}", "").split(" ");
  }

  private void calcularIDFsCorpus(Map<String, Integer> contadorAparicionesPalabra, int documentosTotales) {
    contadorAparicionesPalabra.forEach((palabra, numeroDeApariciones) -> {
      IDFs.put(palabra, log((double) documentosTotales / contadorAparicionesPalabra.get(palabra)));
    });
  }

  private Map<String, Double> calcularTFsDe(String unDocumento) {
    Map<String, Double> TFs = new HashMap<>();

    String[] palabrasEnDocumento = this.parsearPalabras(unDocumento);

    Arrays.stream(palabrasEnDocumento).forEach(palabra -> {
      if (TFs.containsKey(palabra)) {
        TFs.put(palabra, TFs.get(palabra) + 1);
      } else {
        TFs.put(palabra, 1.0);
      }
    });

    TFs.forEach((palabra, cantidadDeAparicionesEnDocumento) -> {
      TFs.put(palabra, cantidadDeAparicionesEnDocumento / palabrasEnDocumento.length);
    });

    return TFs;
  }

  public double[] vectorizar(String documento) {
    Map<String, Double> TFs = calcularTFsDe(documento);
    double[] vector = new double[this.vocabulario.size()];

    for (int i = 0; i < vocabulario.size(); i++) {
      String palabra = vocabulario.get(i);
      if (!TFs.containsKey(palabra)) {
        vector[i] = 0;
      } else {
        vector[i] = TFs.get(palabra) * this.IDFs.get(palabra);
      }
    }

    return vector;
  }
}
