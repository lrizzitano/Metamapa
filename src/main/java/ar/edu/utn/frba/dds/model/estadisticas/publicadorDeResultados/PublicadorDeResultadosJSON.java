package ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PublicadorDeResultadosJSON implements PublicadorDeResultados {
  String nombreArchivo;

  public PublicadorDeResultadosJSON(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }

  public void comunicarResultados(List<ResultadoEstadistico> objetos) {
    if (objetos == null || objetos.isEmpty()) return;

    try (FileWriter writer = new FileWriter(nombreArchivo, true)) {
      writer.write("[\n");

      for (int i = 0; i < objetos.size(); i++) {
        ResultadoEstadistico obj = objetos.get(i);
        Map<String, String> mapa = obj.infoExportable();

        writer.write("  {");
        int j = 0;
        for (Map.Entry<String, String> entry : mapa.entrySet()) {
          String key = escapeJson(entry.getKey());
          String value = escapeJson(entry.getValue());
          writer.write("\"" + key + "\":\"" + value + "\"");
          if (j < mapa.size() - 1) writer.write(",");
          j++;
        }
        writer.write("}");
        if (i < objetos.size() - 1) writer.write(",");
        writer.write("\n");
      }

      writer.write("]\n");
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // escapamos todos los caracteres que romperian el json
  private static String escapeJson(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r");
  }
}
