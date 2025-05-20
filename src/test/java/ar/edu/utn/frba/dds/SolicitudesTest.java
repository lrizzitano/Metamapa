package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

class SolicitudesTest {
  private final Hecho hecho = mock(Hecho.class);
  private final Solicitudes solicitudes = Solicitudes.instance();
  private final Solicitud solicitud = new Solicitud(hecho, "null");

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
}
