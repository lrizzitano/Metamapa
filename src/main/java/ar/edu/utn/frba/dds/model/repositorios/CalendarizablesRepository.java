package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.calendarizables.Calendarizable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarizablesRepository {
  private final Duration margen = Duration.ofMinutes(5);
  private final Set<Calendarizable> calendarizables = new HashSet<>();

  private CalendarizablesRepository() {
  }
  private static final CalendarizablesRepository instance = new CalendarizablesRepository();

  public static CalendarizablesRepository instance() {
    return instance;
  }

  public List<Calendarizable> getPendientesDeActualizar() {
    LocalDateTime ahora  = LocalDateTime.now();
    return this.calendarizables.stream()
        .filter(c -> !c.proximaActualizacion().minus(margen).isBefore(ahora))
        .collect(Collectors.toList());
  }

  public void agregarCalendarizable(Calendarizable calendarizable) {
    this.calendarizables.add(calendarizable);
  }
}
