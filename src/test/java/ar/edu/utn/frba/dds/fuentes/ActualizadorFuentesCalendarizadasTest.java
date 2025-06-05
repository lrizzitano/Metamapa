package ar.edu.utn.frba.dds.fuentes;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ActualizadorFuentesCalendarizadasTest {

  @BeforeEach
  void setUp() {
    ActualizadorFuentesCalendarizadas.instance().reiniciar();
  }

  @Test
  void schedulerLlamaActualizarHechos() throws InterruptedException {
    FuenteProxyCalendarizada fuente = mock(FuenteProxyCalendarizada.class);
    when(fuente.getFrecuencia()).thenReturn(Duration.ofMillis(50));
    ActualizadorFuentesCalendarizadas.instance().suscribir(fuente);
    TimeUnit.MILLISECONDS.sleep(150);
    verify(fuente, atLeastOnce()).actualizarHechos(any());
  }

  @Test
  void desuscribirEvitaLlamadas() throws InterruptedException {
    FuenteProxyCalendarizada fuente = mock(FuenteProxyCalendarizada.class);
    when(fuente.getFrecuencia()).thenReturn(Duration.ofMillis(50));
    ActualizadorFuentesCalendarizadas scheduler = ActualizadorFuentesCalendarizadas.instance();
    scheduler.suscribir(fuente);
    TimeUnit.MILLISECONDS.sleep(150);
    scheduler.desuscribir(fuente);
    clearInvocations(fuente);
    TimeUnit.MILLISECONDS.sleep(150); // wait longer than one period
    verify(fuente, never()).actualizarHechos(any());
  }
}