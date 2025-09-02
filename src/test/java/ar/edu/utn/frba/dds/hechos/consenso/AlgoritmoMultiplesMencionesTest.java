package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlgoritmoMultiplesMencionesTest {

  private final AlgoritmoMultiplesMenciones algoritmoMultiplesMenciones
      = new AlgoritmoMultiplesMenciones();
  private Fuente fuente1;
  private Fuente fuente2;
  private final Hecho hecho1 = new Hecho(null,"t1", "desc1", "cat1", 3.3,
      3.3, LocalDate.now(), LocalDate.now(), Origen.DATASET);
  private Set<Fuente> fuentes;


  @BeforeEach
  public void setUp() {
    fuente1 = mock(Fuente.class);
    fuente2 = mock(Fuente.class);
    fuentes = Set.of(fuente1, fuente2);
  }

  @Test
  void hechoConDosMencionesDistintasNoPasa() {
    final Hecho hecho2 = new Hecho(null,"t1", "desc2", "cat2", 3.3,
        2.1, LocalDate.now().plusDays(1),
        LocalDate.now().minusDays(4), Origen.DATASET);
    final Hecho hecho3 = new Hecho(null,"t1", "desc1", "cat1", 3.3,
        3.3, LocalDate.now(), LocalDate.now(), Origen.DATASET);
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho1));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of(hecho2, hecho3));
    Assertions.assertTrue(algoritmoMultiplesMenciones.getHechosConsensuados(fuentes).isEmpty());
  }

  @Test
  void hechoConUnaMencionNoPasa() {
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho1));
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of());
    Assertions.assertTrue(algoritmoMultiplesMenciones.getHechosConsensuados(fuentes).isEmpty());
  }

  @Test
  void hechoEn2FuentesPasa() {
    final Hecho hecho3 = new Hecho(null,"t1", "desc1", "cat1", 3.3,
        3.3, LocalDate.now(), LocalDate.now(), Origen.DATASET);
    when(fuente1.obtenerHechos(any())).thenReturn(Set.of(hecho1));
    when(fuente2.obtenerHechos(any())).thenReturn(Set.of(hecho3));
    Assertions.assertEquals(hecho1, hecho3);
    Assertions.assertEquals(algoritmoMultiplesMenciones.getHechosConsensuados(fuentes), Set.of(hecho1));
  }
}
