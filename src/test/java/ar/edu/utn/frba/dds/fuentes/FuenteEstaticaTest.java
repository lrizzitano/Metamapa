package ar.edu.utn.frba.dds.fuentes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.execpciones.NoSePudoLeerArchivoException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    Hecho esperado1 = new Hecho("hecho1", "desc1", "cat1", 1.0,
        2.0,  LocalDate.now(), LocalDate.parse("2024-01-01"), Origen.DATASET);
    Hecho esperado2 = new Hecho("hecho2", "desc2", "cat2", 3.0,
        4.0, LocalDate.now(), LocalDate.parse("2024-01-02"), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(hecho -> true);
    Assertions.assertEquals(hechos, Set.of(esperado1, esperado2));
  }

  @Test
  public void filtraCorrectamente() throws IOException{
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho2\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado2 = new Hecho("hecho2", "desc2", "cat2", 3.0,
        4.0, LocalDate.now(), LocalDate.parse("2024-01-02"), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(Filtro.LATITUD_MAYOR.crearFiltro("2"));
    Assertions.assertEquals(hechos, Collections.singleton(esperado2));
  }

  @Test
  public void anteMismoTituloseQuedaElUltimo() throws IOException{
    Files.write(tempFile, List.of(
        "\"titulo\",\"descripcion\",\"categoria\",\"latitud\",\"longitud\",\"fecha\"",
        "\"hecho1\",\"desc1\",\"cat1\",\"1.0\",\"2.0\",\"2024-01-01\"",
        "\"hecho1\",\"desc2\",\"cat2\",\"3.0\",\"4.0\",\"2024-01-02\""
    ));
    Hecho esperado2 = new Hecho("hecho1", "desc2", "cat2", 3.0,
        4.0, LocalDate.now(), LocalDate.parse("2024-01-02"), Origen.DATASET);
    Set<Hecho> hechos = fuente.obtenerHechos(hecho -> true);
    Assertions.assertEquals(hechos, Collections.singleton(esperado2));
  }


}
