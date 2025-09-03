package ar.edu.utn.frba.dds.persistencia;


import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesFuenteDinamicaMemoria;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class SolicitudCambioTest implements SimplePersistenceTest {

  SolicitudesFuenteDinamicaJPA repo = new SolicitudesFuenteDinamicaJPA();

  @Test
  public void persistirSolicitudDeCambio() {
    SolicitudDeCambio solicitud = new SolicitudDeCambio();

    repo.crear(solicitud);
    Assertions.assertNotNull(solicitud.getId());

    Set<SolicitudDeCambio> solicitudesPendientes = repo.getPendientes();
    Assertions.assertTrue(solicitudesPendientes.contains(solicitud));
  }

  @Test
  public void aceptarSolicitudDeCambio() {
    SolicitudDeCambio solicitud = new SolicitudDeCambio();

    repo.crear(solicitud);
    repo.aceptar(solicitud);

    Set<SolicitudDeCambio> solicitudesPendientes = repo.getPendientes();
    Set<SolicitudDeCambio> solicitudesAceptadas = repo.getAceptadas();

    Assertions.assertTrue(solicitudesPendientes.isEmpty());
    Assertions.assertTrue(solicitudesAceptadas.contains(solicitud));

  }
}
