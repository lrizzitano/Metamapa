package ar.edu.utn.frba.dds.calendarizables;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Calendarizable {

  LocalDateTime ultimaActualizaion();

  Duration frecuencia();

  void actualizar();
}
