package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CriterioConsensoTest {
  private final Hecho hecho = new Hecho(null,"hecho1", "desc1", "cat1",
      new Ubicacion(1.0, 2.0, null, null),
      LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);
  private AlgoritmoConsenso algoritmo;
  private Consenso criterio;

  @BeforeEach
  void setUp() {
    algoritmo = mock(AlgoritmoConsenso.class);
    criterio = new Consenso(algoritmo, LocalDateTime.now(), mock(FuentesRepository.class));

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
