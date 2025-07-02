package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CriterioConsensoTest {
  private final Hecho hecho = mock(Hecho.class);
  private AlgoritmoConsenso algoritmo;
  private CriterioConsenso criterio;

  @BeforeEach
  void setUp() {
    algoritmo = mock(AlgoritmoConsenso.class);
    criterio = new CriterioConsenso(algoritmo, LocalDate.now());

  }

  @Test
  void hechoConsensuadoPasa() {
    when(algoritmo.getHechosConsensuados(any())).thenReturn(Set.of(hecho));
    criterio.actualizar();
    Assertions.assertTrue(criterio.esConsensuado(hecho));
  }

  @Test
  void hechoNoConsensuadoNoPasa() {
    when(algoritmo.getHechosConsensuados(any())).thenReturn(Set.of());
    criterio.actualizar();
    Assertions.assertFalse(criterio.esConsensuado(hecho));
  }

  @Test
  void debeEsperarUnaActualizacionParaSerConsensuado() {
    when(algoritmo.getHechosConsensuados(any())).thenReturn(Set.of());
    criterio.actualizar();
    when(algoritmo.getHechosConsensuados(any())).thenReturn(Set.of(hecho));
    Assertions.assertFalse(criterio.esConsensuado(hecho));
    criterio.actualizar();
    Assertions.assertTrue(criterio.esConsensuado(hecho));
  }
}
