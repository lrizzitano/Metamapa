package ar.edu.utn.frba.dds.hechos;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import ar.edu.utn.frba.dds.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.execpciones.SolicitudYaResueltaException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import ar.edu.utn.frba.dds.usuarios.Usuario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class HechosFuenteDinamicaTest {

  private final Hecho unHecho = mock(Hecho.class);
  private HechosFuenteDinamica hechosFuenteDinamica;

  @BeforeEach
  public void setUp() {
    hechosFuenteDinamica = new HechosFuenteDinamica();
  }



  @Test
  void ActualizarHechoEliminaElQueEstabaYAgregaElNuevo()
  {
    hechosFuenteDinamica.agregar(unHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(unHecho));

    Hecho otroHecho = mock(Hecho.class);

    hechosFuenteDinamica.actualizar(unHecho,otroHecho);
    Assertions.assertFalse(hechosFuenteDinamica.obtenerTodos().contains(unHecho));
    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(otroHecho));
  }

  @Test
  void NoPuedoEliminarHechoNoContenidoEnElSet() {

    Assertions.assertThrows(NoSePuedeEliminarUnHechoQueNoExisteException.class, () -> {
      hechosFuenteDinamica.eliminar(unHecho);;});
  }

  @Test
  void agregarHechoLoSumaAlSet() {

    hechosFuenteDinamica.agregar(unHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(unHecho));
  }

  @Test
  void eliminarHechoLoSacaDelSet() {

    hechosFuenteDinamica.agregar(unHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(unHecho));

    hechosFuenteDinamica.eliminar(unHecho);
    Assertions.assertFalse(hechosFuenteDinamica.obtenerTodos().contains(unHecho));
  }

  @Test
  void unHechoArrancaComoNoRevisado() {
    hechosFuenteDinamica.agregar(unHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerNoRevisados().contains(unHecho));
  }

  @Test
  void sePuedeMarcarComoRevisadoUnHecho() {
    hechosFuenteDinamica.agregar(unHecho);
    hechosFuenteDinamica.marcarComoRevisado(unHecho);
    Assertions.assertFalse(hechosFuenteDinamica.obtenerNoRevisados().contains(unHecho));
  }
}
