package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.repositorios.SolicitudesFuenteDinamicaMemoria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SolicitudesFuenteDinamicaTest {

  private final SolicitudDeCambio unaSolicitudDeCambio = mock(SolicitudDeCambio.class);
  private SolicitudesFuenteDinamicaMemoria solicitudesFuenteDinamica;

  @BeforeEach
  public void setUp() {
    solicitudesFuenteDinamica = new SolicitudesFuenteDinamicaMemoria();
  }

  @Test
  void crearSolicitudLaAgregaAPendientes()
  {
    solicitudesFuenteDinamica.crear(unaSolicitudDeCambio);

    Assertions.assertTrue(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
  }

  @Test
  void aceptarSolicitudLaMueveAceptadasYSeVaDePendientes()
  {
    solicitudesFuenteDinamica.crear(unaSolicitudDeCambio);
    solicitudesFuenteDinamica.aceptar(unaSolicitudDeCambio);

    Assertions.assertFalse(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
    Assertions.assertTrue(solicitudesFuenteDinamica.getAceptadas().contains(unaSolicitudDeCambio));
  }

  @Test
  void rechazarSolicitudLaMueveARechazadasGuardaElHechoYSeVaDePendientes()
  {
    Hecho unHecho = mock(Hecho.class);
    when(unaSolicitudDeCambio.getHechoParacambiar()).thenReturn(unHecho);
    solicitudesFuenteDinamica.crear(unaSolicitudDeCambio);
    solicitudesFuenteDinamica.rechazar(unaSolicitudDeCambio);

    Assertions.assertFalse(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
    Assertions.assertTrue(solicitudesFuenteDinamica.getRechazadas().containsKey(unHecho));
  }


}
