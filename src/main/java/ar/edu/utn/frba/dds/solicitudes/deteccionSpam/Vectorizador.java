package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

import static java.lang.Math.log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vectorizador {

  Map<String, Double> mapaPalabrasAIDF = new HashMap<>();

  Vectorizador(List<String> documentos) {
    // el preprocesamiento del corpus de data lo hacemos aca asi se hace al instanciar el vectorizador
    // luego al vectorizar un nuevo documento no repetimos este proceso
    Map<String, Integer> contadorAparicionesPalabra = new HashMap<>();

    documentos.forEach(documento -> procesarDocumento(documento, contadorAparicionesPalabra));
    calcularIDFs(contadorAparicionesPalabra, documentos.size());
  }

  private void procesarDocumento(String documento, Map<String, Integer> contadorAparicionesPalabra) {
    String[] palabras = eliminarSignosDePuntuacion(documento).split(" ");
    Set<String> palabrasYaContadas = new HashSet<>();

    Arrays.stream(palabras)
        .forEach(palabra -> {
          if (!palabrasYaContadas.contains(palabra)) {
            if (contadorAparicionesPalabra.containsKey(palabra)) {
              contadorAparicionesPalabra.put(palabra, contadorAparicionesPalabra.get(palabra) + 1);
            } else {
              contadorAparicionesPalabra.put(palabra, 1);
            }
          }
          palabrasYaContadas.add(palabra);
        });
  }

  private String eliminarSignosDePuntuacion(String texto) {
    return texto.replaceAll("\\p{Punct}", "");
  }

  private void calcularIDFs(Map<String, Integer> contadorAparicionesPalabra, int documentosTotales) {
    contadorAparicionesPalabra.forEach((palabra, numeroDeApariciones) -> {
      mapaPalabrasAIDF.put(palabra, log((double) documentosTotales / contadorAparicionesPalabra.get(palabra)));
    });
  }
}
