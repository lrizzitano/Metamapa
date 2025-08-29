package ar.edu.utn.frba.dds.persistencia;


import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesFuenteDinamica;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class SolicitudCambioTest implements SimplePersistenceTest {

  @Test
  public void persistirSolicitudDeCambio() {

    SolicitudDeCambio solicitud = new SolicitudDeCambio();
    SolicitudesFuenteDinamica repo = new SolicitudesFuenteDinamica();

    repo.persist(solicitud);
    Assertions.assertNotNull(solicitud.getId());

    SolicitudDeCambio otraSolicitud = repo.obetenerSolicitudDeCambio(solicitud.getId());
    Assertions.assertEquals(solicitud, otraSolicitud);


  }
}
