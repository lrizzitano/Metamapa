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
  private LocalDateTime proximaActualizacion;
  private Duration frecuencia;

  public Backup(Path archivo, LocalDateTime proximaActualizacion, Duration frecuencia) {
    this.archivo = archivo;
    this.frecuencia = frecuencia;
    this.gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .setPrettyPrinting().create();
    this.proximaActualizacion = proximaActualizacion;
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return proximaActualizacion;
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
    this.proximaActualizacion = LocalDateTime.now().plus(this.frecuencia);
  }

  public void setFrecuencia(Duration frecuencia) {
    this.frecuencia = frecuencia;
  }
}
