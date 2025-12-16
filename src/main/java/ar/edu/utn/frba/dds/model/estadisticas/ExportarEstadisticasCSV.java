package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.execpciones.ExportarEstadisticasException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExportarEstadisticasCSV {
  // para archivos mas grandes come mucha ram, se podria pasar a hacer streaming de datos en vez de buffering
  public InputStream exportar(List<ResultadoEstadistico> resultados) {
    if (resultados == null || resultados.isEmpty()) return InputStream.nullInputStream();;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {

      // encabezado a partir del primer objeto
      Map<String, String> headers = resultados.get(0).infoExportable();
      baos.write(String.join(",", headers.keySet()).getBytes(StandardCharsets.UTF_8));
      baos.write("\n".getBytes(StandardCharsets.UTF_8));

      // filas
      for (ResultadoEstadistico res : resultados) {
        Map<String, String> fila = res.infoExportable();
        baos.write(String.join(",", fila.values()).getBytes(StandardCharsets.UTF_8));
        baos.write("\n".getBytes(StandardCharsets.UTF_8));
      }

      return new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));

    } catch (IOException e) {
      throw new ExportarEstadisticasException("No se pudo crear el CSV de las estadisticas solicitdas");
    }
  }
}
