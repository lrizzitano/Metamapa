package ar.edu.utn.frba.dds.controllers.utils;

import ar.edu.utn.frba.dds.model.fuentes.metamapa.LocalDateAdapter;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.LocalDateTimeAdapter;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.PathAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonConverter {
  public Gson armarConvertor() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();
  }
}
