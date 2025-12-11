package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.controllers.utils.YoutubeLinkParser;
import ar.edu.utn.frba.dds.model.converters.PathToStringConverter;
import ar.edu.utn.frba.dds.model.execpciones.NoSePudoLeerArchivoException;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import com.opencsv.CSVReaderHeaderAware;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("estatica")
public class FuenteEstatica extends Fuente {
  /*
  Como pre-condición para funcionar correctamente, se asume que el archivo CSV ingresado
  tiene 9 columnas: título, descripción, categoría, latitud, longitud, provincia, fecha, imagen, video
  Además no tiene valores nulos (salvo, tal vez, en video) y la fecha respeta el formato yyyy-mm-dd
  */
  public FuenteEstatica() {}

  @Convert(converter = PathToStringConverter.class)
  @Column(name = "path_estatica" ,nullable = true)
  private Path path;

  @Column(name = "ultima_modificacion_estatica")
  private LocalDateTime ultimaModificacion;

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
          .toLocalDateTime();
    } catch (Exception e) {
      throw new NoSePudoLeerArchivoException(
          "Error al obtener la fecha de modificación del archivo "
              + path + " : " + e.getMessage());
    }
  }

  @Override
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
    Hecho hecho = new Hecho(
        null,
        fila.get("titulo"),
        fila.get("descripcion"),
        fila.get("categoria"),
        new Ubicacion(
            Double.parseDouble(fila.get("latitud")),
            Double.parseDouble(fila.get("longitud")),
            Provincia.parseProvincia(fila.get("provincia")),
            null),
        ultimaModificacion,
        LocalDate.parse(fila.get("fecha")).atStartOfDay(),
        Origen.DATASET
    );
    hecho.setImagen(fila.get("imagen"));
    if (fila.get("video") != null && !fila.get("video").isBlank()) {
      // todo: desacoplarse del formato de youtube
      hecho.setVideo(YoutubeLinkParser.obtenerVideoEmbebible(fila.get("video")));
    }

    return hecho;
  }

  @Override
  public String getNombre(){
    return "Estatica";
  }

  @Override
  public String detalle() {
    return "Path de archivo de origen: " + path.getFileName().toString();
  }
}
