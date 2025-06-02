package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuenteProxyCalendarizadaTest {

  static class TestFuente extends FuenteProxyCalendarizada {
    private final Queue<Hecho> hechosQueue = new LinkedList<>();

    public TestFuente(Duration refreshTime) {
      super(refreshTime);
    }

    public void addHecho(Hecho hecho) {
      hechosQueue.add(hecho);
    }

    @Override
    protected Set<Hecho> getNewHechos(Instant ultimaLlamada) {
      Hecho hecho;
      Set<Hecho> hechos = new HashSet<>();
      while((hecho = hechosQueue.poll()) != null) {
        hechos.add(hecho);
      }
      return hechos;
    }
  }

  @Test
  void acumulaHechos() throws InterruptedException {
    Hecho hecho = mock(Hecho.class);
    TestFuente fuente = new TestFuente(Duration.ofMillis(10*1000));
    fuente.addHecho(hecho);
    fuente.init();
    Thread.sleep(200);
    Assertions.assertEquals(Collections.singleton(hecho), fuente.obtenerHechos(new NullFiltro()));
  }

  @Test
  void filtrahecho() throws InterruptedException {
    Hecho hecho1 = mock(Hecho.class);
    Hecho hecho2 = mock(Hecho.class);
    when(hecho1.categoria()).thenReturn("hola");
    when(hecho2.categoria()).thenReturn("NotHola");
    TestFuente fuente = new TestFuente(Duration.ofMillis(10*1000));
    fuente.addHecho(hecho1);
    fuente.addHecho(hecho2);
    fuente.init();
    Thread.sleep(200);
    Assertions.assertEquals(Collections.singleton(hecho1),
        fuente.obtenerHechos(new FiltroCategoria("hola")));
  }


  @Test
  void noAcumulaAntesDeTiempo() throws InterruptedException {
    Hecho hecho = mock(Hecho.class);
    TestFuente fuente = new TestFuente(Duration.ofMillis(1000000));
    fuente.addHecho(hecho);
    fuente.init();
    Thread.sleep(200);
    fuente.addHecho(mock(Hecho.class));
    Assertions.assertEquals(Collections.singleton(hecho), fuente.obtenerHechos(new NullFiltro()));
  }
}
