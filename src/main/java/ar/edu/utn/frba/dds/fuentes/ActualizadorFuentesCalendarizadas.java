package ar.edu.utn.frba.dds.fuentes;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ActualizadorFuentesCalendarizadas {
  private static final ActualizadorFuentesCalendarizadas instance
      = new ActualizadorFuentesCalendarizadas();

  private final Set<FuenteProxyCalendarizada> fuentesSuscriptas = new HashSet<>();
  private Instant ultimaLlamada = Instant.now();
  private Timer timer;
  private Duration intervalo = Duration.ofSeconds(1);

  public static ActualizadorFuentesCalendarizadas instance() {
    return instance;
  }

  private ActualizadorFuentesCalendarizadas() {
    iniciarTimer();
  }

  public void setIntervalo(Duration tiempo) {
    intervalo = tiempo;
    iniciarTimer();
  }

  private void iniciarTimer() {
    if (timer != null) {
      timer.cancel();
    }

    TimerTask timerTask = new TimerTask() {

      @Override
      public void run() {
        fuentesSuscriptas.forEach(f ->
            f.actualizarHechos(ultimaLlamada));

        ultimaLlamada = Instant.now();
      }
    };

    timer = new Timer("Timer");
    timer.schedule(timerTask, 0, intervalo.toMillis());
  }

  public void reiniciar() {
    this.fuentesSuscriptas.clear();
    timer.cancel();
    ultimaLlamada = Instant.now();
    iniciarTimer();
  }

  public void suscribir(FuenteProxyCalendarizada fuente) {
    fuentesSuscriptas.add(fuente);
  }

  public void desuscribir(FuenteProxyCalendarizada fuente) {
    fuentesSuscriptas.remove(fuente);
  }
}
