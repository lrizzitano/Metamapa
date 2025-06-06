package ar.edu.utn.frba.dds.fuentes;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ActualizadorFuentesCalendarizadasTest {

  @BeforeEach
  void setUp() {
    ActualizadorFuentesCalendarizadas.instance().reiniciar();
    ActualizadorFuentesCalendarizadas.instance().setIntervalo(Duration.ofMillis(50));
  }

  @Test
  void schedulerLlamaActualizarHechos() throws InterruptedException {
    FuenteProxyCalendarizada fuente = mock(FuenteProxyCalendarizada.class);
    ActualizadorFuentesCalendarizadas.instance().suscribir(fuente);
    TimeUnit.MILLISECONDS.sleep(190);
    verify(fuente, atLeast(3)).actualizarHechos(any());
  }

  @Test
  void desuscribirEvitaLlamadas() throws InterruptedException {
    FuenteProxyCalendarizada fuente = mock(FuenteProxyCalendarizada.class);
    ActualizadorFuentesCalendarizadas scheduler = ActualizadorFuentesCalendarizadas.instance();
    scheduler.suscribir(fuente);
    TimeUnit.MILLISECONDS.sleep(100);
    verify(fuente, atLeastOnce()).actualizarHechos(any());
    scheduler.desuscribir(fuente);
    clearInvocations(fuente);
    TimeUnit.MILLISECONDS.sleep(100); // wait longer than one period
    verify(fuente, never()).actualizarHechos(any());
  }
}