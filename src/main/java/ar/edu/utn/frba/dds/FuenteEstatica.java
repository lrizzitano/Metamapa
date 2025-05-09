package ar.edu.utn.frba.dds;

import com.opencsv.CSVReaderHeaderAware;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.function.Predicate;

public class FuenteEstatica implements Fuente {
  /*
  Como pre-condición para funcionar correctamente, se asume que el archivo CSV ingresado
  tiene 6 columnas: título, descripción, categoría, latitud, longitud, fecha
  Además no tiene valores nulos y la fecha respeta el formato yyyy-mm-dd
  */
  private final String pathArchivo;

  public FuenteEstatica(String pathArchivo) {
    this.pathArchivo = pathArchivo;
  }

  @Override
  public Set<Hecho> obtenerHechos(Predicate<Hecho> filtro){
    Map<String, Hecho> hechosPorTitulo = new HashMap<>();
    try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(this.pathArchivo))) {
      Map<String, String> fila;
      while ((fila = reader.readMap()) != null) {
        Hecho hecho = this.crearHechoDesdeFila(fila);
        if (filtro.test(hecho)) {
          hechosPorTitulo.put(hecho.titulo(), hecho);
        }
      }
    } catch (Exception e) {
      throw new NoSePudoLeerArchivoException(e.getMessage());
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
      LocalDate.now(),
      LocalDate.parse(fila.get("fecha")),
      Origen.DATASET
    );
  }
}
