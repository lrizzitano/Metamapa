package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.Solicitudes.Solicitudes;
import ar.edu.utn.frba.dds.Usuarios.Administrador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SolicitudesTest {
  private final Hecho hecho = mock(Hecho.class);
  private final Solicitudes solicitudes = Solicitudes.instance();
  private Solicitud solicitud;

  @BeforeEach
  void setUp() {
    solicitudes.reset();
    solicitud = new Solicitud(hecho, "null");
  }

  @Test
  void contieneSolicitudPendiente(){
    Assertions.assertTrue(solicitudes.getPendientes().contains(solicitud));
  }

  @Test
  void aceptoSolicitud(){;
    solicitud.aceptar(null);
    Assertions.assertTrue(solicitudes.getAceptadas().contains(solicitud));
  }

  @Test
  void rechazoSolicitud(){
    solicitud.rechazar(null);
    Solicitud solicitud2 = new Solicitud(hecho, "null");
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
    new Solicitud(hecho, "spam spam");
    Assertions.assertEquals(1, solicitudes.getRechazos(hecho));
    solicitudes.setDetectorDeSpam(null);
  }
}
