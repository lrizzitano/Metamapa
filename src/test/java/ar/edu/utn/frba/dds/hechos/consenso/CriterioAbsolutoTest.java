package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.FuentesRepository;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CriterioAbsolutoTest {
  private final CriterioAbsoluto criterioAbsoluto = new CriterioAbsoluto();
  private Fuente fuente1;
  private Fuente fuente2;
  private final Hecho hecho = mock(Hecho.class);
  private final FuentesRepository fuentes = FuentesRepository.instance();


  @BeforeEach
  public void setUp() {
    fuente1 = mock(Fuente.class);
    fuente2 = mock(Fuente.class);
    fuentes.agregarFuente(fuente1);
    fuentes.agregarFuente(fuente2);
  }

  @AfterEach
  public void tearDown() {
    fuentes.reset();
  }

  @Test
  void hechoEnTodasLasFuentesPasa() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of(hecho));
    criterioAbsoluto.actualizar();
    Assertions.assertEquals(Set.of(hecho), criterioAbsoluto.getHechosConsensuados());
  }

  @Test
  void hechoEn1FuenteNoPasa() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of());
    criterioAbsoluto.actualizar();
    Assertions.assertTrue(criterioAbsoluto.getHechosConsensuados().isEmpty());
  }
}
