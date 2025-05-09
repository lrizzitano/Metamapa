package ar.edu.utn.frba.dds;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormateadorCsvTest {

  private static final Path ARCHIVO_SALIDA = Paths.get("fires-all-formateado.csv");

  @Test
  public void EjecutarFormateadorCreaArchivoFormateado() {
    FormateadorCsv.main(null);
    Assertions.assertTrue(Files.exists(ARCHIVO_SALIDA));
  }
}
