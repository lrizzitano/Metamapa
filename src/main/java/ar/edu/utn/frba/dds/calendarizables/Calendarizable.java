package ar.edu.utn.frba.dds.calendarizables;

import java.time.LocalDateTime;

public interface Calendarizable {

  LocalDateTime proximaActualizacion();

  void actualizar();
}
