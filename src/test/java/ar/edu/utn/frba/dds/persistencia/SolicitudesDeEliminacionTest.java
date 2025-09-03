package ar.edu.utn.frba.dds.persistencia;

import ar.edu.utn.frba.dds.repositorios.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class SolicitudesDeEliminacionTest implements SimplePersistenceTest {

  SolicitudesDeEliminacionJPA repo = new SolicitudesDeEliminacionJPA();

  // TODO: Arreglar test
  @Disabled
  @Test
  public void persistirSolicitudDeEliminacion() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion();

    repo.nuevaSolicitud(solicitud);
    entityManager().flush();
    Assertions.assertNotNull(solicitud.getId());

    Set<SolicitudDeEliminacion> solicitudesPendientes = repo.getPendientes();
    Set<SolicitudDeEliminacion> solicitudesAceptadas = repo.getAceptadas();

    System.out.println("Tamaño: " + solicitudesPendientes.size());
    System.out.println("Tamaño aceptadas: " + solicitudesAceptadas.size());
    solicitudesPendientes.stream().forEach(s -> System.out.println(s.getId()));
    Assertions.assertTrue(solicitudesPendientes.contains(solicitud));
  }

  @Test
  public void aceptarSolicitudDeEliminacion() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion();

    repo.nuevaSolicitud(solicitud);
    repo.aceptarSolicitud(solicitud);

    Set<SolicitudDeEliminacion> solicitudesPendientes = repo.getPendientes();
    Set<SolicitudDeEliminacion> solicitudesAceptadas = repo.getAceptadas();

    Assertions.assertTrue(solicitudesPendientes.isEmpty());
    Assertions.assertTrue(solicitudesAceptadas.contains(solicitud));

  }

}
