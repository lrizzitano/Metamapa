package ar.edu.utn.frba.dds;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ar.edu.utn.frba.dds.Fuentes.FuenteProxyAsincronica;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuenteProxyAsincronicaTest {

  static class TestFuente extends FuenteProxyAsincronica {
    private final Queue<Hecho> hechosQueue = new LinkedList<>();

    public TestFuente(Duration refreshTime) {
      super(refreshTime);
    }

    public void addHecho(Hecho hecho) {
      hechosQueue.add(hecho);
    }

    @Override
    protected List<Hecho> getNextHecho(Instant ultimaLlamada) {
      Hecho hecho = hechosQueue.poll();
      return hecho == null ? null : Collections.singletonList(hecho);
    }
  }

  @Test
  void acumulaHechos() throws InterruptedException {
    Hecho hecho = mock(Hecho.class);
    TestFuente fuente = new TestFuente(Duration.ofMillis(10*1000));
    fuente.addHecho(hecho);
    fuente.init();
    Thread.sleep(200);
    Assertions.assertEquals(Collections.singleton(hecho), fuente.obtenerHechos(h -> true));
  }

  @Test
  void filtrahecho() throws InterruptedException {
    Hecho hecho1 = mock(Hecho.class);
    Hecho hecho2 = mock(Hecho.class);
    when(hecho1.titulo()).thenReturn("hola");
    when(hecho2.titulo()).thenReturn("NotHola");
    TestFuente fuente = new TestFuente(Duration.ofMillis(10*1000));
    fuente.addHecho(hecho1);
    fuente.addHecho(hecho2);
    fuente.init();
    Thread.sleep(200);
    Assertions.assertEquals(Collections.singleton(hecho1),
        fuente.obtenerHechos(hecho -> hecho.titulo().equals("hola")));
  }


  @Test
  void noAcumulaAntesDeTiempo() throws InterruptedException {
    Hecho hecho = mock(Hecho.class);
    TestFuente fuente = new TestFuente(Duration.ofMillis(1000000));
    fuente.addHecho(hecho);
    fuente.init();
    Thread.sleep(200);
    fuente.addHecho(mock(Hecho.class));
    Assertions.assertEquals(Collections.singleton(hecho), fuente.obtenerHechos(h -> true));
  }


}
