package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Hechos.Origen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class HechoTest {
  private final Hecho unHecho = new Hecho(
      "Incendio forestal",
      "Gran incendio en zona rural",
      "Desastre natural",
      -34.6037,
      -58.3816,
      LocalDate.of(2023, 12, 15),
      LocalDate.of(2023, 11, 30),
      Origen.DATASET);

  @Test
  public void SeCargoTitulo() {
    Assertions.assertEquals("Incendio forestal",unHecho.titulo());
  }

  @Test
  public void SeCargoDescricion() {
    Assertions.assertEquals("Gran incendio en zona rural",unHecho.descripcion());
  }

  @Test
  public void SeCargoCategoria() {
    Assertions.assertEquals("Desastre natural",unHecho.categoria());
  }

  @Test
  public void SeCargoLatitud() {
    Assertions.assertEquals(-34.6037,unHecho.latitud());
  }

  @Test
  public void SeCargoLongitud() {
    Assertions.assertEquals(-58.3816,unHecho.longitud());
  }

  @Test
  public void SeCargoFechaCarga() {
    Assertions.assertEquals(LocalDate.of(2023, 12, 15),unHecho.fechaCarga());
  }

  @Test
  public void SeCargoFechaAcontecimiento() {
    Assertions.assertEquals(LocalDate.of(2023, 11, 30),unHecho.fechaAcontecimiento());
  }

  @Test
  public void SeCargoOrigen() {
    Assertions.assertEquals(Origen.DATASET,unHecho.origen());
  }
}
