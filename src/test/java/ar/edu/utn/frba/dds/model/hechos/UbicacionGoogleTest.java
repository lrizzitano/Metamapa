package ar.edu.utn.frba.dds.model.hechos;

import ar.edu.utn.frba.dds.model.hechos.ubicadorGoogle.UbicadorGoogle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UbicacionGoogleTest {
  private final ServicioUbicador ubicador = new UbicadorGoogle();

  @Test
  public void detectaLasCoordenadasDeSanMartinEnBuenosAires() {
    assertEquals(Provincia.BUENOS_AIRES, ubicador.getProvincia(-34.566666666667, -58.516666666667));
  }

  @Test
  public void detectaBarilocheEnRioNegro() {
    assertEquals(Provincia.RIO_NEGRO, ubicador.getProvincia(-41.15,-71.3 ));
  }
}