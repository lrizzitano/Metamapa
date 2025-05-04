package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class SolicitudesTest {
  private Solicitudes solicitudes;
  private Solicitud solicitud;

  @BeforeEach
  public void setUp() {
    solicitudes = Solicitudes.instance();
    solicitud = new Solicitud(mock(Hecho.class), "porque sí");
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
    Assertions.assertTrue(solicitudes.getRechazadas().contains(solicitud));
  }

  @Test
  void seEliminaElHecho(){
    solicitud.aceptar(null);
    Assertions.assertTrue(solicitudes.hechosEliminados().contains(solicitud.getHecho()));
  }

  @Test
  void seAcuerdaQuienLaAcepto(){
    Administrador admin = new Administrador();
    solicitud.aceptar(admin);
    Assertions.assertEquals(solicitud.getResponsable(), admin);
  }
}
