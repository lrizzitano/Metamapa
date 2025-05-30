package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.solicitudes.DetectorDeSpam;
import ar.edu.utn.frba.dds.solicitudes.Solicitudes;
import ar.edu.utn.frba.dds.Usuarios.Administrador;
import java.util.function.Predicate;

import ar.edu.utn.frba.dds.hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class FiltroCompuestoBuilderTest {
  private FiltroCompuestoBuilder builder;
  private final Predicate<Hecho> siempreTrue = hecho -> true;
  private final Predicate<Hecho> siempreFalse = hecho -> false;
  private final Hecho hecho = mock(Hecho.class);

  @BeforeEach
  void setUp() {
    builder = new FiltroCompuestoBuilder();
  }

  @Test
  void filtroVacioSiempreCumple(){
    Predicate<Hecho> filtroAnd = builder.componerFiltrosAnd();
    Predicate<Hecho> filtroOr = builder.componerFiltrosOr();
    Assertions.assertTrue(filtroAnd.test(hecho));
    Assertions.assertTrue(filtroOr.test(hecho));
  }

  @Test
  void trueMasFalseNoCumpleAnd(){
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreFalse);
    Predicate<Hecho>  filtro = builder.componerFiltrosAnd();
    Assertions.assertFalse(filtro.test(hecho));
  }

  @Test
  void trueMasTrueCumpleAnd(){
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreTrue);
    Predicate<Hecho>  filtro = builder.componerFiltrosAnd();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void falseMasFalseNoCumpleOr() {
    builder.agregarFiltro(siempreFalse).agregarFiltro(siempreFalse);
    Predicate<Hecho> filtro = builder.componerFiltrosOr();
    Assertions.assertFalse(filtro.test(hecho));
  }

  @Test
  void trueMasFalseCumpleOr() {
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreFalse);
    Predicate<Hecho> filtro = builder.componerFiltrosOr();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void trueMasTrueCumpleOr() {
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreTrue);
    Predicate<Hecho> filtro = builder.componerFiltrosOr();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void seNiegaElFiltro(){
    builder.agregarFiltro(siempreTrue).negarFiltros();
    Predicate<Hecho> filtro = builder.componerFiltrosAnd();
    Assertions.assertFalse(filtro.test(hecho));
  }

  static class SolicitudesTest {
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
}
