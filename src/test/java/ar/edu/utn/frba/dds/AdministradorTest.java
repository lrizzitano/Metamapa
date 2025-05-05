package ar.edu.utn.frba.dds;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AdministradorTest {
  private final Administrador unAdministrador = new Administrador("JUANMA","RAMA",90);
  private final Solicitudes solicitudes = Solicitudes.instance();
  private final Solicitud unaSolicitud = new Solicitud(mock(Hecho.class), null);

  @Test
  public void AdminRechazaSolicitud_EsResonsable(){
    unAdministrador.aceptarSolicitud(unaSolicitud);
    Assertions.assertEquals(unAdministrador,unaSolicitud.getResponsable());
  }

  @Test
  public void AdminAceotaSolicitud_EsResonsable(){
    unAdministrador.rechazarSolicitud(unaSolicitud);
    Assertions.assertEquals(unAdministrador,unaSolicitud.getResponsable());
  }
}
