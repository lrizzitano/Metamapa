package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.execpciones.NoSePudoLeerArchivoException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import com.opencsv.CSVReaderHeaderAware;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;


public class FuenteEstatica implements Fuente {
  /*
  Como pre-condición para funcionar correctamente, se asume que el archivo CSV ingresado
  tiene 6 columnas: título, descripción, categoría, latitud, longitud, fecha
  Además no tiene valores nulos y la fecha respeta el formato yyyy-mm-dd
  */
  private final Path path;
  private LocalDate ultimaModificacion;

  public FuenteEstatica(String path) {
    this.path = Paths.get(path);
    if (!Files.exists(this.path)) {
      throw new NoSePudoLeerArchivoException("Archivo no encontrado");
    }
    this.actualizarFechaModificacion();
  }

  private void actualizarFechaModificacion() {
    try {
      ultimaModificacion = Files.getLastModifiedTime(path)
          .toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
    } catch (Exception e) {
      throw new NoSePudoLeerArchivoException(
          "Error al obtener la fecha de modificación del archivo "
              + path + " : " + e.getMessage());
    }
  }

  public Set<Hecho> obtenerHechos(Filtro filtro) {
    Map<String, Hecho> hechosPorTitulo = new HashMap<>();
    Predicate<Hecho> filtroPredicate = filtro.getAsPredicate();
    this.actualizarFechaModificacion();
    try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(
        Files.newBufferedReader(path, java.nio.charset.StandardCharsets.UTF_8))) {
      Map<String, String> fila;
      while ((fila = reader.readMap()) != null) {
        Hecho hecho = this.crearHechoDesdeFila(fila);
        if (filtroPredicate.test(hecho)) {
          hechosPorTitulo.put(hecho.titulo(), hecho);
        }
      }
    } catch (Exception e) {
      throw new NoSePudoLeerArchivoException("Error al leer el archivo " + path + " : "
          + e.getMessage());
    }
    return new HashSet<>(hechosPorTitulo.values());
  }

  public Hecho crearHechoDesdeFila(Map<String, String> fila) {
    return new Hecho(
        fila.get("titulo"),
        fila.get("descripcion"),
        fila.get("categoria"),
        Double.parseDouble(fila.get("latitud")),
        Double.parseDouble(fila.get("longitud")),
        ultimaModificacion,
        LocalDate.parse(fila.get("fecha")),
        Origen.DATASET
    );
  }
}
