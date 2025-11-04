package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import io.javalin.http.Context;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.Map;

public class HomeController implements WithSimplePersistenceUnit, TransactionalOps {
  public Map<String, Object> show(Context ctx)  {
    Map<String, Object> model = new HashMap<>();

    withTransaction(() -> {
      model.put("colecciones", new ColeccionesRepository().findAll());
    });

    return model;
  }
}
