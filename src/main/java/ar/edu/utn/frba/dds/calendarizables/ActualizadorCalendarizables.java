package ar.edu.utn.frba.dds.calendarizables;

public class ActualizadorCalendarizables {
  public static void main(String[] args) {
    System.out.println("Voy a actualizar!");
    CalendarizablesRepository repo = CalendarizablesRepository.instance();
    repo.getPendientesDeActualizar().forEach(c -> {
      try {
        c.actualizar();
      }
      catch (Exception e) {
        reportarExcepcion(e);
      }
    });
  }

  private static void reportarExcepcion(Exception e) {
    e.printStackTrace();
  }
}
