package ar.edu.utn.frba.dds.model.hechos;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HechoTest {
  private final Hecho unHecho = new Hecho(
      null,
      "Incendio forestal",
      "Gran incendio en zona rural",
      "Desastre natural",
      new Ubicacion(-34.6037,
           -58.3816,
          null,
          null),
      LocalDateTime.of(2023, 12, 15,11,23),
      LocalDateTime.of(2023, 11, 30,11,23),
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
    Assertions.assertEquals(LocalDateTime.of(2023, 12, 15,11,23),unHecho.fechaCarga());
  }

  @Test
  public void SeCargoFechaAcontecimiento() {
    Assertions.assertEquals(LocalDateTime.of(2023, 11, 30,11,23),unHecho.fechaAcontecimiento());
  }

  @Test
  public void SeCargoOrigen() {
    Assertions.assertEquals(Origen.DATASET,unHecho.origen());
  }
}
