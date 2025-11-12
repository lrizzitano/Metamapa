package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AgregadorTest {
  private Agregador agregador;
  private Fuente fuente1;
  private Fuente fuente2;
  private final Hecho hecho1 = mock(Hecho.class);
  private final Hecho hecho2 = mock(Hecho.class);

  @BeforeEach
  public void setUp() {
    fuente1 = mock(Fuente.class);
    fuente2 = mock(Fuente.class);
    Set<Fuente> fuentes = Set.of(fuente1, fuente2);
    agregador = new Agregador(fuentes, LocalDateTime.now(), Duration.ZERO);
  }

  /* CAMBIO EL DOMINIO Y LA CACHE NO SIRVE
  @Test
  void noActualizaSiNoLeDigo() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho1));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of(hecho2));
    Assertions.assertNotEquals(Set.of(hecho1, hecho2), agregador.obtenerHechos(new NullFiltro()));
  }*/

  @Test
  void traeHechosDeTodasSusFuentes() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho1));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of(hecho2));
    agregador.actualizar();
    Assertions.assertEquals(Set.of(hecho1, hecho2), agregador.obtenerHechos(new NullFiltro()));
  }
}
