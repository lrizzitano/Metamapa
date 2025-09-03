package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.SolicitudesDeEliminacion;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.NullDetector;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SolicitudesTest {
  private final Hecho hecho = mock(Hecho.class);
  private final SolicitudesDeEliminacion solicitudes = SolicitudesDeEliminacion.instance();
  private SolicitudDeEliminacion solicitud;

  @BeforeEach
  void setUp() {
    solicitudes.reset();
    solicitudes.setDetectorDeSpam(new NullDetector());
    solicitud = new SolicitudDeEliminacion(hecho, "null");
  }

  @Test
  void contieneSolicitudPendiente(){
    Assertions.assertTrue(solicitudes.getPendientes().contains(solicitud));
  }

  @Test
  void aceptoSolicitud(){
    solicitud.aceptar(null);
    Assertions.assertTrue(solicitudes.getAceptadas().contains(solicitud));
  }

  @Test
  void rechazoSolicitud(){
    solicitud.rechazar(null);
    SolicitudDeEliminacion solicitud2 = new SolicitudDeEliminacion(hecho, "null");
    solicitud2.rechazar(null);
    Assertions.assertEquals(2, solicitudes.getRechazadas().get(solicitud.getHecho()));
  }

  @Test
  void seEliminaElHecho(){
    solicitud.aceptar(null);
    Assertions.assertTrue(solicitudes.hechosEliminados().contains(solicitud.getHecho()));
  }

  @Test
  void seEliminaElSolicitud(){
    solicitud.aceptar(null);
    Assertions.assertTrue(solicitudes.estaEliminado(hecho));
  }

  @Test
  void seAcuerdaQuienLaAcepto(){
    Administrador admin = mock(Administrador.class);
    solicitud.aceptar(admin);
    Assertions.assertEquals(solicitud.getResponsable(), admin);
  }

  @Test
  void seRechazaElSpam() {
    DetectorDeSpam detectorDeSpam = mock(DetectorDeSpam.class);
    when(detectorDeSpam.esSpam(any())).thenReturn(true);
    solicitudes.setDetectorDeSpam(detectorDeSpam);
    new SolicitudDeEliminacion(hecho, "spam spam");
    Assertions.assertEquals(1, solicitudes.getRechazos(hecho));
  }
}
