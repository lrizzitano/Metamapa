package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.hechos.Ubicacion;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.NullDetector;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;

class SolicitudesTest implements SimplePersistenceTest {
  private final Hecho hecho = new Hecho(
      null,
      "Incendio forestal",
      "Gran incendio en zona rural",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15),
      LocalDate.of(2023, 11, 30),
      Origen.DATASET);

  private final SolicitudesDeEliminacionJPA solicitudes = new SolicitudesDeEliminacionJPA();
  private final HechosFuenteDinamicaJPA hechos = new HechosFuenteDinamicaJPA();
  private final RepoUsuarios repoUsuarios = new RepoUsuarios();
  private SolicitudDeEliminacion solicitud =  new SolicitudDeEliminacion(hecho.titulo(), "null");
  private Administrador administrador = new Administrador();

  @BeforeEach
  void setUp() {
    hechos.agregar(hecho);
    repoUsuarios.save(administrador);
    solicitudes.setDetectorDeSpam(new NullDetector());
  }

  @Test
  void seAcuerdaQuienLaAcepto(){
    solicitud.aceptar(administrador);
    Assertions.assertEquals(solicitud.getResponsable(), administrador);
  }

  @Test
  void seAcuerdaQuienLaRechazo(){
    solicitud.rechazar(administrador);
    Assertions.assertEquals(solicitud.getResponsable(), administrador);
  }

  @Test
  void noSePuedeAceptarSolicitudQueYaFueAceptada(){
    solicitud.aceptar(administrador);

    Assertions.assertThrows(SolicitudYaResueltaException.class, () -> {
      solicitud.aceptar(administrador);;
    });
  }

  @Test
  void noSePuedeRechazarSolicitudQueYaFueRechazada(){
    solicitud.rechazar(administrador);

    Assertions.assertThrows(SolicitudYaResueltaException.class, () -> {
      solicitud.rechazar(administrador);;
    });
  }


}
