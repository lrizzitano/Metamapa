package ar.edu.utn.frba.dds.persistencia;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class SolicitudesDeEliminacionTest implements SimplePersistenceTest {

  SolicitudesDeEliminacionJPA repoSolicitudes = new SolicitudesDeEliminacionJPA();
  HechosFuenteDinamicaJPA repoHechos = new HechosFuenteDinamicaJPA();

  @Test
  public void persistirSolicitudDeEliminacion() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion();

    repoSolicitudes.nuevaSolicitud(solicitud);
    Assertions.assertNotNull(solicitud.getId());

    Set<SolicitudDeEliminacion> solicitudesPendientes = repoSolicitudes.getPendientes();

    Assertions.assertTrue(solicitudesPendientes.contains(solicitud));
  }

  @Test
  public void aceptarSolicitudDeEliminacion() {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion();

    repoSolicitudes.nuevaSolicitud(solicitud);
    repoSolicitudes.aceptarSolicitud(solicitud);

    Set<SolicitudDeEliminacion> solicitudesPendientes = repoSolicitudes.getPendientes();
    Set<SolicitudDeEliminacion> solicitudesAceptadas = repoSolicitudes.getAceptadas();

    Assertions.assertTrue(solicitudesPendientes.isEmpty());
    Assertions.assertTrue(solicitudesAceptadas.contains(solicitud));

  }

  @Test
  public void detectarHechoEliminado() {
    Hecho hecho = new Hecho();
    repoHechos.agregar(hecho);

    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho, "No me gusto, bajenlon");
    repoSolicitudes.nuevaSolicitud(solicitud);

    Assertions.assertFalse(repoSolicitudes.estaEliminado(hecho));
    repoSolicitudes.aceptarSolicitud(solicitud);
    Assertions.assertTrue(repoSolicitudes.estaEliminado(hecho));
  }

  @Test
  public void detectarSolicitudesRechazadas() {
    Hecho hecho = new Hecho();
    repoHechos.agregar(hecho);

    SolicitudDeEliminacion solicitud1 = new SolicitudDeEliminacion(hecho, "No me gusto, bajenlon");
    SolicitudDeEliminacion solicitud2 = new SolicitudDeEliminacion(hecho, "Malisimo esto, cortala pipo");
    repoSolicitudes.nuevaSolicitud(solicitud1);
    repoSolicitudes.nuevaSolicitud(solicitud2);

    Assertions.assertTrue(repoSolicitudes.getRechazadas().isEmpty());
    Assertions.assertEquals(0, repoSolicitudes.getRechazos(hecho));

    repoSolicitudes.rechazarSolicitud(solicitud1);
    repoSolicitudes.rechazarSolicitud(solicitud2);

    Assertions.assertFalse(repoSolicitudes.getRechazadas().isEmpty());
    Assertions.assertEquals(2, repoSolicitudes.getRechazos(hecho));
  }

}
