package ar.edu.utn.frba.dds.fuentes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarizablesRepository {

  Set<Calendarizable> fuentesCalendarizables = new HashSet<>();

  private CalendarizablesRepository() {}
  private static final CalendarizablesRepository instance = new CalendarizablesRepository();

  public List<Calendarizable> getPendientesDeActulizar() {
    return this.fuentesCalendarizables.stream()
        .filter(Calendarizable::tocaActulizar)
        .collect(Collectors.toList());
  }

  public void agregarCalendarizable(Calendarizable calendarizable) {
    this.fuentesCalendarizables.add(calendarizable);
  }
}
