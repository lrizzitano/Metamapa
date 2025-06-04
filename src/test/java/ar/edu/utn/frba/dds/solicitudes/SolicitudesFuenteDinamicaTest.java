package ar.edu.utn.frba.dds.solicitudes;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.usuarios.Usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class SolicitudesFuenteDinamicaTest {

  private final SolicitudDeCambio unaSolicitudDeCambio = mock(SolicitudDeCambio.class);
  private  SolicitudesFuenteDinamica solicitudesFuenteDinamica;

  @BeforeEach
  public void setUp() {
    solicitudesFuenteDinamica = new SolicitudesFuenteDinamica();
  }

  @Test
  void crearSolicitudLaAgregaAPendientes()
  {
    solicitudesFuenteDinamica.crear(unaSolicitudDeCambio);

    Assertions.assertTrue(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
  }

  @Test
  void aceptarSolicitudLaMueveARechazadasYSeVaDePendientes()
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
    when(unaSolicitudDeCambio.getHechoACambiar()).thenReturn(unHecho);
    solicitudesFuenteDinamica.crear(unaSolicitudDeCambio);
    solicitudesFuenteDinamica.rechazar(unaSolicitudDeCambio);

    Assertions.assertFalse(solicitudesFuenteDinamica.getPendientes().contains(unaSolicitudDeCambio));
    Assertions.assertTrue(solicitudesFuenteDinamica.getRechazadas().containsKey(unHecho));
  }


}
