package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.execpciones.SolicitudDeCambioInvalidaException;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SolicitudDeCambioTest {

  private final Usuario unUsuario = mock(Usuario.class);
  private final Hecho hechoAModificar = mock(Hecho.class);
  private final Hecho hechoModificado = mock(Hecho.class);

  @Test
  void NoPuedoCrearSolicitudDeCambioSiPasaron7Dias() {

    when(hechoAModificar.fechaCarga()).thenAnswer(invocation -> LocalDate.parse("2024-01-02").atStartOfDay());

    assertThrows(SolicitudDeCambioInvalidaException.class, ()
        -> new SolicitudDeCambio(hechoAModificar, hechoModificado, unUsuario));
  }

  @Test
  void NoPuedoCrearSolicitudSiUsuarioNoEstaRegistrado() {
    when(hechoAModificar.fechaCarga()).thenReturn(LocalDateTime.now());
    when(unUsuario.estaRegistrado()).thenReturn(false);

    assertThrows(SolicitudDeCambioInvalidaException.class, ()
        -> new SolicitudDeCambio(hechoAModificar, hechoModificado, unUsuario));
  }

  @Test
  void NoPuedoCrearSolicitudSiUsuarioNoEsQuienSubioElHecho() {
    Usuario otroUsuario = mock(Usuario.class);

    when(hechoAModificar.fechaCarga()).thenReturn(LocalDateTime.now());
    when(unUsuario.estaRegistrado()).thenReturn(true);
    when(hechoAModificar.contribuyente()).thenReturn(otroUsuario);

    assertThrows(SolicitudDeCambioInvalidaException.class, () ->
      new SolicitudDeCambio(hechoAModificar, hechoModificado, unUsuario)
    );

  }
}
