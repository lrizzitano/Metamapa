package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.metamapa.LocalDateAdapter;
import ar.edu.utn.frba.dds.fuentes.metamapa.PathAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Backup implements Calendarizable {

  private final Path archivo;
  private final Gson gson;
  private final Fuente fuenteDinamica = FuenteDinamica.instance();
  private final Duration frecuencia = Duration.ofDays(1);
  private LocalDateTime ultimaActualizacion = LocalDateTime.now();

  public Backup(Path archivo) {
    this.archivo = archivo;
    this.gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();
  }

  @Override
  public LocalDateTime ultimaActualizaion() {
    return this.ultimaActualizacion;
  }

  @Override
  public Duration frecuencia() {
    return this.frecuencia;
  }

  @Override
  public void actualizar() { //ESTO ES BACKUPEAR
    try {
      Files.createDirectories(archivo.getParent());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    try (var writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
      gson.toJson(this.fuenteDinamica.obtenerHechos(new NullFiltro()), writer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    this.ultimaActualizacion = LocalDateTime.now();
  }
}
