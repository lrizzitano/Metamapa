package ar.edu.utn.frba.dds.fuentes;



public class ActualizadorFuentesCalendarizadas {
  public static void main(String[] args) {
    CalendarizablesRepository repo = CalendarizablesRepository.instance();
    repo.getPendientesDeActualizar().forEach(fuente -> {
      try {
        fuente.actualizar();
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
