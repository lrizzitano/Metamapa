package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.server.SetupData;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Router implements WithSimplePersistenceUnit {

  public void routearApp(Javalin app) {
    new SetupData().setup();


    app.before(ctx -> entityManager().getTransaction().begin());

    app.after(ctx -> entityManager().getTransaction().commit());


    app.get("/", ctx -> {
      Map<String, Object> model = new HashMap<>();
      model.put("colecciones", new ColeccionesRepository().findAll());
      ctx.render("templates/mapaPagina/mapaPagina", model);
    });

    app.get("/{id}", ctx -> {
      Long id = Long.parseLong(ctx.pathParam("id"));
      Coleccion coleccion = new ColeccionesRepository().find(id);
      coleccion.setSolicitudes(new SolicitudesDeEliminacionJPA());
      Map<String, Object> model = new HashMap<>();
      model.put("hechos", coleccion.hechos(new NullFiltro()));
      ctx.render("templates/hechos", model);
    });
  }
}
