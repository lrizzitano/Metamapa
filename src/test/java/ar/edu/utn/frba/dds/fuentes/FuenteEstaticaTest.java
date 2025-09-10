package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ar.edu.utn.frba.dds.execpciones.NoSePudoLeerArchivoException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.hechos.Ubicacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

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
    Hecho esperado1 = new Hecho(null,"titulo1", "desc1", "cat1",
        new Ubicacion(1.0, 2.0, null, null),
        LocalDate.now(), LocalDate.now(), Origen.DATASET);
    Hecho esperado2 = new Hecho(null,"titulo2", "desc2", "cat2",
        new Ubicacion(2.0, 4.0, null, null),
        LocalDate.now().plusDays(1), LocalDate.now().minusDays(3),
        Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(new NullFiltro());

    assertThat(hechos)
        .usingRecursiveComparison()
        .isEqualTo(Set.of(esperado2, esperado1));
  }

  @Test
  public void filtraCorrectamente() throws IOException{
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho2\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado1 = new Hecho(null, "hecho1", "desc1", "cat1",         new Ubicacion(-34.6037,
        -58.3816, null, null), LocalDate.now(), LocalDate.parse("2024-01-01"), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(new FiltroFechaHasta(LocalDate.parse("2024-01-02")));

    assertThat(hechos)
        .usingRecursiveComparison()
        .isEqualTo(Collections.singleton(esperado1));
  }

  @Test
  public void anteMismoTituloseQuedaElUltimo() throws IOException{
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho1\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado2 = new Hecho(null, "hecho1", "desc2", "cat2",
        new Ubicacion(-34.6037,
        -58.3816, null, null),
        LocalDate.now(), LocalDate.parse("2024-01-02"), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(new NullFiltro());

    assertThat(hechos)
        .usingRecursiveComparison()
        .isEqualTo(Collections.singleton(esperado2));
  }


}
