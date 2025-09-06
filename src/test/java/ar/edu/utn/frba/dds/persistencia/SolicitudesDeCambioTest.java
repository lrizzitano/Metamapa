package ar.edu.utn.frba.dds.persistencia;


import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class SolicitudesDeCambioTest implements SimplePersistenceTest {

  SolicitudesFuenteDinamicaJPA repoSolicitudes = new SolicitudesFuenteDinamicaJPA();
  HechosFuenteDinamicaJPA repoHechos = new HechosFuenteDinamicaJPA();
  RepoUsuarios repoUsuarios = new RepoUsuarios();


  @Test
  public void persistirSolicitudDeCambio() {
    SolicitudDeCambio solicitud = new SolicitudDeCambio();

    repoSolicitudes.nuevaSolicitud(solicitud);
    Assertions.assertNotNull(solicitud.getId());

    Set<SolicitudDeCambio> solicitudesPendientes = repoSolicitudes.getPendientes();
    Assertions.assertTrue(solicitudesPendientes.contains(solicitud));
  }

  @Test
  public void aceptarSolicitudDeCambio() {
    SolicitudDeCambio solicitud = new SolicitudDeCambio();

    repoSolicitudes.nuevaSolicitud(solicitud);
    repoSolicitudes.aceptarSolicitud(solicitud);

    Set<SolicitudDeCambio> solicitudesPendientes = repoSolicitudes.getPendientes();
    Set<SolicitudDeCambio> solicitudesAceptadas = repoSolicitudes.getAceptadas();

    Assertions.assertTrue(solicitudesPendientes.isEmpty());
    Assertions.assertTrue(solicitudesAceptadas.contains(solicitud));

  }

  @Test
  public void detectarSolicitudesRechazadas() {

    Usuario  usuario = new Usuario("Peperino", "Pomoro", 43);
    repoUsuarios.save(usuario);

    Hecho hecho = new Hecho(
        null,
        "Incendio forestal",
        "Gran incendio en zona rural",
        "Desastre natural",
        -34.6037,
        -58.3816,
        LocalDate.now(),
        LocalDate.of(2023, 11, 30),
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
}
