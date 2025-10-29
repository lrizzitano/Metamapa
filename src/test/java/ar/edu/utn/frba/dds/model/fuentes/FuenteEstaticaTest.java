package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.execpciones.NoSePudoLeerArchivoException;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuenteEstaticaTest{

  private Path tempFile;
  private FuenteEstatica fuente;

  @BeforeEach
  public void setup() throws IOException{
    tempFile = Files.createTempFile("hechos", ".csv");
    fuente = new FuenteEstatica(tempFile.toString());
  }

  @AfterEach
  public void teardown() throws IOException{
    Files.deleteIfExists(tempFile);
  }

  @Test
  public void noSePuedeCrearUnaFuenteDeUnArchivoInvalido(){
    Assertions.assertThrows(NoSePudoLeerArchivoException.class, () -> new FuenteEstatica("nada"));
  }

  @Test
  public void devuelveLosHechos() throws IOException {
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho2\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado1 = new Hecho(null,"hecho1", "desc1", "cat1",
        new Ubicacion(1.0, 2.0, null, null),
        LocalDateTime.now(), LocalDateTime.now(), Origen.DATASET);
    Hecho esperado2 = new Hecho(null,"hecho2", "desc2", "cat2",
        new Ubicacion(2.0, 4.0, null, null),
        LocalDateTime.now().plusDays(1), LocalDateTime.now().minusDays(3),
        Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(new NullFiltro());

    assertThat(hechos).isEqualTo(Set.of(esperado2, esperado1));
  }

  @Test
  public void filtraCorrectamente() throws IOException{
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho2\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado1 = new Hecho(null, "hecho1", "desc1", "cat1",         new Ubicacion(-34.6037,
        -58.3816, null, null), LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(new FiltroFechaHasta(LocalDate.parse("2024-01-02").atStartOfDay()));

    assertEquals(hechos, Collections.singleton(esperado1));
  }

  @Test
  public void anteMismoTituloseQuedaElUltimo() throws IOException{
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho1\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado2 = new Hecho(null, "hecho1", "desc2", "cat2",
        new Ubicacion(-34.6037, -58.3816, null, null),
        LocalDateTime.now(), LocalDate.parse("2024-01-02").atStartOfDay(), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(new NullFiltro());

    assertEquals(hechos, Collections.singleton(esperado2));
  }


}
