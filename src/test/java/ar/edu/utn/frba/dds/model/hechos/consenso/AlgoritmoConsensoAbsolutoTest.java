package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlgoritmoConsensoAbsolutoTest {
  private final AlgoritmoConsensoAbsoluto criterioAbsoluto = new AlgoritmoConsensoAbsoluto();
  private Fuente fuente1;
  private Fuente fuente2;
  private final Hecho hecho = new Hecho(null,"hecho1", "desc1", "cat1",
      new Ubicacion(-34.6037, -58.3816, null, null),
      LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);
  private Set<Fuente> fuentes;


  @BeforeEach
  public void setUp() {
    fuente1 = mock(Fuente.class);
    fuente2 = mock(Fuente.class);
    fuentes = Set.of(fuente1, fuente2);
  }

  @Test
  void hechoEnTodasLasFuentesPasa() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of(hecho));
    Assertions.assertEquals(Set.of(hecho), criterioAbsoluto.getHechosConsensuados(fuentes));
  }

  @Test
  void hechoEn1FuenteNoPasa() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of());
    Assertions.assertTrue(criterioAbsoluto.getHechosConsensuados(fuentes).isEmpty());
  }
}
