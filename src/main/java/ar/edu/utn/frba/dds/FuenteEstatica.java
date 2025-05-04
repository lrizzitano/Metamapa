package ar.edu.utn.frba.dds;

import com.opencsv.CSVReaderHeaderAware;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.function.Predicate;

public class FuenteEstatica implements Fuente {
  private final String pathArchivo;

  public FuenteEstatica(String pathArchivo) {
    this.pathArchivo = pathArchivo;
  }

  @Override
  public Set<Hecho> obtenerHechos(Predicate<Hecho> filtro){
    Set<Hecho> hechos = new HashSet<>();
    try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(this.pathArchivo))) {
      Map<String, String> fila;
      while ((fila = reader.readMap()) != null) {
        Hecho hecho = this.crearHechoDesdeFila(fila);
        if (filtro.test(hecho) && !this.estaRepetido(hecho, hechos)) {
          hechos.add(hecho);
        }
      }
    } catch (Exception e) {
      throw new NoSePudoLeerArchivoException(e.getMessage());
    }
    return hechos;
  }

  public Hecho crearHechoDesdeFila(Map<String, String> fila) {
    return new Hecho(
      fila.get("titulo"),
      fila.get("descripcion"),
      fila.get("categoria"),
      Double.parseDouble(fila.get("latitud")),
      Double.parseDouble(fila.get("longitud")),
      LocalDate.now(),
      LocalDate.parse(fila.get("fecha")), //TODO: ver tema formato
      Origen.DATASET
    );
  }

  public Boolean estaRepetido(Hecho unHecho, Set<Hecho> hechos) {
    return hechos.stream().anyMatch(hecho -> hecho.titulo().equals(unHecho.titulo()));
  }
}
