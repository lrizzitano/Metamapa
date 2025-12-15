package ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PublicadorDeResultadosCSV implements PublicadorDeResultados{
  public String nombreArchivo;

  public PublicadorDeResultadosCSV(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }

  @Override
  public void comunicarResultados(List<ResultadoEstadistico> resultados) {
    if (resultados == null || resultados.isEmpty()) return;

    try (FileWriter writer = new FileWriter(nombreArchivo, true)) {
      File archivo = new File(nombreArchivo);
      if (!archivo.exists() || archivo.length() == 0) {
        // encabezado a partir del primer objeto
        Map<String, String> headers = resultados.get(0).infoExportable();
        writer.append(String.join(",", headers.keySet()));
        writer.append("\n");
      }

      // filas
      for (ResultadoEstadistico res : resultados) {
        Map<String, String> fila = res.infoExportable();
        writer.append(String.join(",", fila.values()));
        writer.append("\n");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
