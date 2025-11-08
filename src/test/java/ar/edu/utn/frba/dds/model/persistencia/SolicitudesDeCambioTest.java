package ar.edu.utn.frba.dds.model.persistencia;


import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class SolicitudesDeCambioTest implements SimplePersistenceTest {

  SolicitudesFuenteDinamicaJPA repoSolicitudes = new SolicitudesFuenteDinamicaJPA();
  HechosFuenteDinamicaJPA repoHechos = new HechosFuenteDinamicaJPA();
  RepoUsuarios repoUsuarios = new RepoUsuarios();
  SolicitudDeCambio solicitudVacia = new SolicitudDeCambio();

  @Test
  public void persistirSolicitudDeCambio_LaAgregaAPendientes() {


    repoSolicitudes.nuevaSolicitud(solicitudVacia);
    Assertions.assertNotNull(solicitudVacia.getId());

    Set<SolicitudDeCambio> solicitudesPendientes = repoSolicitudes.getPendientes();
    Assertions.assertTrue(solicitudesPendientes.contains(solicitudVacia));
  }

  @Test
  public void aceptarSolicitudDeCambio_PasaAEstarAceptada() {


    repoSolicitudes.nuevaSolicitud(solicitudVacia);
    repoSolicitudes.aceptarSolicitud(solicitudVacia);

    Set<SolicitudDeCambio> solicitudesPendientes = repoSolicitudes.getPendientes();
    Set<SolicitudDeCambio> solicitudesAceptadas = repoSolicitudes.getAceptadas();

    Assertions.assertTrue(solicitudesPendientes.isEmpty());
    Assertions.assertTrue(solicitudesAceptadas.contains(solicitudVacia));

  }

  @Test
  public void detectarContadorDeRechazosDeUnHecho() {

    Usuario  usuario = new Usuario("pepe", "Peperino", "Pomoro", LocalDate.now(), "A");
    repoUsuarios.save(usuario);

    Hecho hecho = new Hecho(
        null,
        "Incendio forestal",
        "Gran incendio en zona rural",
        "Desastre natural",
        new Ubicacion(-34.6037,
            -58.3816, null, null),
        LocalDateTime.now(),
        LocalDateTime.of(2023, 11, 30,11,11),
        Origen.DATASET);
    hecho.setContribuyente(usuario);
    repoHechos.agregar(hecho);

    SolicitudDeCambio solicitud1 = new SolicitudDeCambio(hecho, hecho,  usuario);
    SolicitudDeCambio solicitud2 = new SolicitudDeCambio(hecho, hecho, usuario);
    repoSolicitudes.nuevaSolicitud(solicitud1);
    repoSolicitudes.nuevaSolicitud(solicitud2);

    Assertions.assertTrue(repoSolicitudes.getRechazadas().isEmpty());
    Assertions.assertEquals(0, repoSolicitudes.getRechazos(hecho));

    repoSolicitudes.rechazarSolicitud(solicitud1);
    repoSolicitudes.rechazarSolicitud(solicitud2);

    Assertions.assertFalse(repoSolicitudes.getRechazadas().isEmpty());
    Assertions.assertEquals(2, repoSolicitudes.getRechazos(hecho));
  }

  @Test
  void rechazarSolicitud_PasaAEstarRechazada()
  {
    repoSolicitudes.nuevaSolicitud(solicitudVacia);
    repoSolicitudes.rechazarSolicitud(solicitudVacia);

    Assertions.assertTrue(repoSolicitudes.getPendientes().isEmpty());
    Assertions.assertTrue(repoSolicitudes.getAceptadas().isEmpty());
    Assertions.assertEquals(1, repoSolicitudes.getRechazadas().size());

  }

}

/*
    private final SolicitudDeCambio unaSolicitudDeCambio = mock(SolicitudDeCambio.class);
  private SolicitudesFuenteDinamicaJPA solicitudesFuenteDinamica;

  @BeforeEach
  public void setUp() {
    solicitudesFuenteDinamica = new SolicitudesFuenteDinamicaJPA();
  }

  @Test
  void crearSolicitudLaAgregaAPendientes()
  {
    solicitudesFuenteDinamica.nuevaSolicitud(unaSolicitudDeCambio);


    verify(solicitudesFuenteDinamica).
    //Assertions.assertTrue(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
  }

  @Test
  void aceptarSolicitudLaMueveAceptadasYSeVaDePendientes()
  {
    solicitudesFuenteDinamica.nuevaSolicitud(unaSolicitudDeCambio);
    solicitudesFuenteDinamica.aceptarSolicitud(unaSolicitudDeCambio);

    Assertions.assertFalse(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
    Assertions.assertTrue(solicitudesFuenteDinamica.getAceptadas().contains(unaSolicitudDeCambio));
  }

  @Test
  void rechazarSolicitudLaMueveARechazadasGuardaElHechoYSeVaDePendientes()
  {
    Hecho unHecho = mock(Hecho.class);
    when(unaSolicitudDeCambio.getHechoParacambiar()).thenReturn(unHecho);
    solicitudesFuenteDinamica.nuevaSolicitud(unaSolicitudDeCambio);
    solicitudesFuenteDinamica.rechazarSolicitud(unaSolicitudDeCambio);

    Assertions.assertFalse(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
    Assertions.assertTrue(solicitudesFuenteDinamica.getRechazadas().containsKey(unHecho));
  }


 */

