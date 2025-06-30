package ar.edu.utn.frba.dds.calendarizables;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarizablesRepository {

  Set<Calendarizable> fuentesCalendarizables = new HashSet<>();

  private CalendarizablesRepository() {}
  private static final CalendarizablesRepository instance = new CalendarizablesRepository();

  public static CalendarizablesRepository instance() {return instance;}

  public List<Calendarizable> getPendientesDeActualizar() {
    return this.fuentesCalendarizables.stream()
        .filter(c -> {
          Duration transcurrido = Duration.between(c.ultimaActualizaion(), LocalDateTime.now());
          Duration margen = c.frecuencia().minus(Duration.ofMinutes(5));
          return !transcurrido.minus(margen).isNegative();
        }).collect(Collectors.toList());
  }

  public void agregarCalendarizable(Calendarizable calendarizable) {
    this.fuentesCalendarizables.add(calendarizable);
  }
}
