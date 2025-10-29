package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.execpciones.NoSePudoLeerArchivoException;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FormateadorCsv {

  private static final String ARCHIVO_ENTRADA = "fires-all.csv";
  // podria hacerse por parametro, para demo usamos estos
  private static final String ARCHIVO_SALIDA = "fires-all-formateado.csv";

  public static void main(String[] args) {
    int filasInvalidas = 0;
    int filasValidas = 0;
    try (
        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(
            java.nio.file.Files.newBufferedReader(
                java.nio.file.Paths.get(ARCHIVO_ENTRADA), StandardCharsets.UTF_8)
        );
        CSVWriter writer = new CSVWriter(
            java.nio.file.Files.newBufferedWriter(
                java.nio.file.Paths.get(ARCHIVO_SALIDA), StandardCharsets.UTF_8)
        )

    ) {
      writer.writeNext(new String[]{"titulo", "descripcion", "categoria",
          "latitud", "longitud", "fecha"});

      Map<String, String> fila;
      while ((fila = reader.readMap()) != null) {
        if (filaInvalida(fila)) {
          filasInvalidas++;
          continue;
        }
        filasValidas++;

        String titulo = generarTitulo(fila);
        String descripcion = generarDescripcion(fila);

        List<String> filaSalida = Arrays.asList(
            titulo,
            descripcion,
            "Incendio forestal", //categoria
            fila.get("lat"),
            fila.get("lng"),
            fila.get("fecha")
        );

        writer.writeNext(filaSalida.toArray(new String[0]));
      }
    } catch (IOException | CsvValidationException e) {
      throw new NoSePudoLeerArchivoException(e.getMessage());
    }

    System.out.printf("Se leyeron %d filas validas%n", filasValidas);
    System.out.printf("Se ignoraron %d filas por tener campos invalidos%n", filasInvalidas);
  }

  private static boolean filaInvalida(Map<String, String> fila) {
    return esInvalido(fila.get("fecha"))
        || esInvalido(fila.get("lat"))
        || esInvalido(fila.get("lng"))
        || esInvalido(fila.get("municipio"))
        || fila.get("municipio").equalsIgnoreCase("INDETERMINADO")
        || esInvalido(fila.get("id"));
  }

  private static boolean esInvalido(String campo) {
    return campo == null || campo.isBlank();
  }

  private static String generarTitulo(Map<String, String> fila) {
    return String.format(
        "Incendio Forestal en %s de %s hectareas - id: %s",
        fila.get("municipio"),
        fila.getOrDefault("superficie", "?"),
        fila.get("id")
    );
  }

  private static String generarDescripcion(Map<String, String> fila) {
    String fecha = fila.get("fecha");
    String municipio = fila.get("municipio");
    String superficie = fila.getOrDefault("superficie", "?");

    String desc = String.format(
        "En la fecha %s se generó un incendio forestal"
            + "en el municipio de %s de una magnitud de %s hectáreas.",
        fecha, municipio, superficie)
        + parsearCampoCondicional(fila.get("muertos"), "muertos")
        + parsearCampoCondicional(fila.get("heridos"), "heridos")
        + parsearTiempo("control", fila.get("time_ctrl"))
        + parsearTiempo("extinción", fila.get("time_ext"));

    return desc.trim();
  }

  private static String parsearCampoCondicional(String valor, String palabra) {
    if (valor == null) {
      return "";
    }
    if (valor.equals("0")) {
      return "No se registraron " + palabra + ". ";
    }
    return String.format("Se registraron %s %s. ", valor, palabra);
  }

  private static String parsearTiempo(String tipo, String valor) {
    if (valor == null || valor.equals("0")) {
      return "";
    }
    return String.format(
        "Se registró un tiempo hasta tener el fuego bajo %s de %s minutos. ",
        tipo, valor);
  }
}
