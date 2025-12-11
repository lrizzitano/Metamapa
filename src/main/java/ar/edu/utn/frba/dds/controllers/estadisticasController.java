package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class estadisticasController {

  public static void landing(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("contenido", false);
    ctx.render("templates/paginas/estadisticas");
  }
}
