package ar.edu.utn.frba.dds.solicitudes;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.usuarios.Usuario;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class SolicitudDeCambioTest {

  private final Usuario unUsuario = mock(Usuario.class);
  private final Hecho hechoAModificar = mock(Hecho.class);
  private final Hecho hechoModificado = mock(Hecho.class);

  @Test
  void NoPuedoCrearSolicitudDeCambioSiPasaron7Dias() {

    when(hechoAModificar.fechaCarga()).thenAnswer(invocation -> LocalDate.parse("2024-01-02"));

    assertThrows(SolicitudDeCambioInvalidaException.class, () -> {
      new SolicitudDeCambio(hechoAModificar, hechoModificado, unUsuario);
    });
  }

  @Test
  void NoPuedoCrearSolicitudSiUsuarioNoEstaRegistrado() {
    when(hechoAModificar.fechaCarga()).thenReturn(LocalDate.now());
    when(unUsuario.estaRegistrado()).thenReturn(false);

    assertThrows(SolicitudDeCambioInvalidaException.class, () -> {
      new SolicitudDeCambio(hechoAModificar, hechoModificado, unUsuario);
    });
  }

  @Test
  void NoPuedoCrearSolicitudSiUsuarioNoEsQuienSubioElHecho() {
    Usuario otroUsuario = mock(Usuario.class);

    when(hechoAModificar.fechaCarga()).thenReturn(LocalDate.now());
    when(unUsuario.estaRegistrado()).thenReturn(true);
    when(hechoAModificar.contribuyente()).thenReturn(otroUsuario);

    assertThrows(SolicitudDeCambioInvalidaException.class, () -> {
      new SolicitudDeCambio(hechoAModificar, hechoModificado, unUsuario);
    });
  }
}
