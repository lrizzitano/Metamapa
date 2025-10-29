package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LectorDataset {

  public static List<Solicitud> cargarSolicitudes(String rutaArchivo) {
    List<Solicitud> solicitudes = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new FileReader(rutaArchivo))) {
      List<String[]> filas = reader.readAll();

      for (int i = 1; i < filas.size(); i++) { // saltar encabezado
        String[] fila = filas.get(i);
        if (fila.length >= 2) {
          Solicitud s = new Solicitud(fila[0].toLowerCase(), fila[1].equals("spam") ? Categoria.SPAM : Categoria.NO_SPAM);
          solicitudes.add(s);
        }
      }

    } catch (Exception e) {
      throw new RuntimeException("No se pudo leer el csv");
    }

    return solicitudes;
  }
}
