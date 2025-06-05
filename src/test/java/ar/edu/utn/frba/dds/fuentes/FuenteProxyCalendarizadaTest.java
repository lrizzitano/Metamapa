package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuenteProxyCalendarizadaTest {
  private final Hecho unHecho = mock(Hecho.class);
  private final Hecho otroHecho = mock(Hecho.class);

  static class TestFuente extends FuenteProxyCalendarizada {
    private final Set<Hecho> hechos = new HashSet<>();

    public TestFuente() {
    }

    public void addHecho(Hecho hecho) {
      hechos.add(hecho);
    }

    protected Set<Hecho> getNewHechos(Instant ultimaLlamada) {
      return hechos;
    }
  }


  @Test
  void acumulaHechos() {
    TestFuente fuente = new TestFuente();
    fuente.addHecho(unHecho);
    fuente.actualizarHechos(Instant.now());
    Assertions.assertEquals(Collections.singleton(unHecho), fuente.obtenerHechos(new NullFiltro()));
    fuente.addHecho(otroHecho);
    fuente.actualizarHechos(Instant.now());
    Assertions.assertEquals(Set.of(unHecho, otroHecho), fuente.obtenerHechos(new NullFiltro()));
  }

  @Test
  void filtrahecho() {
    TestFuente fuente = new TestFuente();
    when(unHecho.categoria()).thenReturn("hola");
    when(otroHecho.categoria()).thenReturn("not hola");
    fuente.addHecho(unHecho);
    fuente.addHecho(otroHecho);
    fuente.actualizarHechos(Instant.now());
    Assertions.assertEquals(Collections.singleton(unHecho),
        fuente.obtenerHechos(new FiltroCategoria("hola")));
  }


  @Test
  void noAcumulaPorSiSola() {
    TestFuente fuente = new TestFuente();
    fuente.addHecho(unHecho);
    fuente.actualizarHechos(Instant.now());
    fuente.addHecho(otroHecho);
    Assertions.assertEquals(Collections.singleton(unHecho), fuente.obtenerHechos(new NullFiltro()));
  }
}
