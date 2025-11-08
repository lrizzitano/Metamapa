package ar.edu.utn.frba.dds.model.persistencia;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.RechazosDeEliminacion;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam.NullDetector;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SolicitudesDeEliminacionTest implements SimplePersistenceTest {

  SolicitudesDeEliminacionJPA repoSolicitudes = new SolicitudesDeEliminacionJPA();
  HechosFuenteDinamicaJPA repoHechos = new HechosFuenteDinamicaJPA();
  private final Hecho hecho = new Hecho(
      null,
      "Incendio forestal",
      "Gran incendio en zona rural",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDateTime.of(2023, 12, 15,6,9),
      LocalDateTime.of(2023, 11, 30,7,8),
      Origen.DATASET);

  private final SolicitudesDeEliminacionJPA solicitudes = new SolicitudesDeEliminacionJPA();
  private final HechosFuenteDinamicaJPA hechos = new HechosFuenteDinamicaJPA();
  private final RepoUsuarios repoUsuarios = new RepoUsuarios();
  private SolicitudDeEliminacion solicitud =  new SolicitudDeEliminacion(hecho.titulo(), "null");
  private Administrador administrador = new Administrador("a","a","a",LocalDate.now(), "a");
  private DetectorDeSpam detectorSpam;

  @BeforeEach
  void setUp() {
    hechos.agregar(hecho);
    repoUsuarios.save(administrador);
    solicitudes.setDetectorDeSpam(new NullDetector());
    detectorSpam = mock(DetectorDeSpam.class);
  }

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
    Hecho hecho = new Hecho(null,"hecho1", "desc1", "cat1",
        new Ubicacion(-34.6037, -58.3816, null, null),
        LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);
    repoHechos.agregar(hecho);

    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(hecho.titulo(), "No me gusto, bajenlon");
    repoSolicitudes.nuevaSolicitud(solicitud);

    Assertions.assertFalse(repoSolicitudes.estaEliminado(hecho.titulo()));
    repoSolicitudes.aceptarSolicitud(solicitud);
    Assertions.assertTrue(repoSolicitudes.estaEliminado(hecho.titulo()));
  }

  @Test
  public void detectarSolicitudesRechazadas() {
    Hecho hecho = new Hecho(null,"hecho1", "desc1", "cat1",
        new Ubicacion(-34.6037, -58.3816, null, null),
        LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);

    repoHechos.agregar(hecho);

    SolicitudDeEliminacion solicitud1 = new SolicitudDeEliminacion(hecho.titulo(), "No me gusto, bajenlon");
    SolicitudDeEliminacion solicitud2 = new SolicitudDeEliminacion(hecho.titulo(), "Malisimo esto, cortala pipo");
    repoSolicitudes.nuevaSolicitud(solicitud1);
    repoSolicitudes.nuevaSolicitud(solicitud2);

    Assertions.assertTrue(repoSolicitudes.getRechazadas().isEmpty());
    Assertions.assertEquals(0, repoSolicitudes.getRechazos(hecho.titulo()));

    repoSolicitudes.rechazarSolicitud(solicitud1);
    repoSolicitudes.rechazarSolicitud(solicitud2);

    Assertions.assertFalse(repoSolicitudes.getRechazadas().isEmpty());
    Assertions.assertEquals(2, repoSolicitudes.getRechazos(hecho.titulo()));
  }

  @Test
  public void detectarSolicitudesRechazadasPorSpam() {
    when(detectorSpam.esSpam(solicitud.getFundamento())).thenReturn(true);
    repoSolicitudes.setDetectorDeSpam(detectorSpam);

    Assertions.assertTrue(repoSolicitudes.getRechazadas().isEmpty());

    repoSolicitudes.nuevaSolicitud(solicitud);
    Set<RechazosDeEliminacion> listaRechazos = repoSolicitudes.getRechazadas();

    Assertions.assertEquals(1, listaRechazos.size());
    Assertions.assertEquals(1, listaRechazos.iterator().next().getCantidadRechazadas());
    Assertions.assertEquals(1, listaRechazos.iterator().next().getCantidadSpam());
  }



}
