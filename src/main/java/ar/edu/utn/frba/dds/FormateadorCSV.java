package ar.edu.utn.frba.dds;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class FormateadorCSV {

  public static void main(String[] args) {
    String archivoEntrada = "fires-all.csv";
    String archivoSalida = "fires-all-formateado.csv";

    try (
        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(archivoEntrada));
        CSVWriter writer = new CSVWriter(new FileWriter(archivoSalida))
    ) {
      // Escribimos cabeceras en el archivo de salida
      writer.writeNext(new String[]{"titulo", "descripcion", "categoria", "latitud", "longitud", "fecha"});

      Map<String, String> fila;

      while ((fila = reader.readMap()) != null) {

        String fecha = fila.get("fecha");
        String lat = fila.get("lat");
        String lng = fila.get("lng");
        String superficie = fila.get("superficie");
        String municipio = fila.get("municipio");
        String id = fila.get("id");
        String muertos = fila.get("muertos");
        String heridos = fila.get("heridos");
        String time_ctrl = fila.get("time_ctrl");
        String time_ext = fila.get("time_ext");

        if (fecha == null ||
            lat == null ||
            lng == null ||
            municipio == null || municipio.equals("INDETERMINADO") ||
            id == null) {
          continue;
        }
        List<String> filaSalida = new ArrayList<>();

        String titulo = String.format("Incendio Forestal en %s de %s hectareas - id: %s",
            municipio, superficie, id);

        filaSalida.add(titulo);

        StringBuilder descripcion = new StringBuilder();

        descripcion.append(String.format("""
        En la fecha %s se genero un incendio forestal en el municipio de %s de una magnitud de %s hectareas.
        """, fecha, municipio, superficie));

        if (muertos != null) {
          if (muertos.equals("0")) {
            descripcion.append("No se registraron muertos. ");
          } else {
            descripcion.append(String.format("Se registraron %s muertos. ", muertos));
          }
        }

        if (heridos != null) {
          if (heridos.equals("0")) {
            descripcion.append("No se registraron heridos. ");
          } else {
            descripcion.append(String.format("Se registraron %s heridos. ", heridos));
          }
        }

        if (time_ctrl != null) {
          if (!time_ctrl.equals("0")) {
            descripcion.append(String.format("""
                Se registro un tiempo hasta tener el fuego bajo control de %s minutos
                """, time_ctrl));
          }
        }

        if (time_ext != null) {
          if (!time_ext.equals("0")) {
            descripcion.append(String.format("""
                Se registro un tiempo hasta tener el fuego extinto de %s minutos
                """, time_ext));
          }
        }

        String descripcionFinal = descripcion.toString();

        filaSalida.add(descripcionFinal);

        filaSalida.add("Incendio forestal");
        filaSalida.add(lat);
        filaSalida.add(lng);
        filaSalida.add(fecha);

        writer.writeNext(filaSalida.toArray(new String[0]));
      }

    } catch (Exception e) {
      throw new RuntimeException("Error al procesar archivos CSV", e);
    }
  }
}
