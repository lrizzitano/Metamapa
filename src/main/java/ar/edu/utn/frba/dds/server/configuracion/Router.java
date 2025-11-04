package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.server.SetupData;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;

public class Router implements WithSimplePersistenceUnit {

  public void routearApp(Javalin app) {
    new SetupData().setup();
    HomeController homeController = new HomeController();
    ColeccionesController coleccionesController = new ColeccionesController();

    app.before(ctx -> {
      entityManager().clear();
    });

    app.get("/", ctx ->
      ctx.render("templates/paginas/mapa/mapaPagina", homeController.show(ctx)));

    app.get("/colecciones/{id}/hechos",
        ctx -> ctx.render("templates/paginas/mapa/hechos", coleccionesController.hechos(ctx)));

    //Soy boludo o no encontre forma mejor idk
    app.get("navegar/hechos/nuevo", ctx ->
      ctx.header("HX-Redirect","/hechos/nuevo"));

    app.get("/hechos/nuevo", ctx -> {
      ctx.render("templates/paginas/subirHecho");
    });

    app.get("navegar/solicitudesDeEliminacion/nueva", ctx -> {
      String hecho = ctx.queryParam("hecho");
      ctx.header("HX-Redirect","/solicitudesDeEliminacion/nueva?hecho=" + hecho);
    });

    app.get("/solicitudesDeEliminacion/nueva", ctx -> {
      String paramHecho = ctx.queryParam("hecho");
      Map<String, Object> model = new HashMap<>();
      model.put("hecho", paramHecho);
      ctx.render("templates/paginas/nuevaSolicitudDeEliminacion", model);
    });

    app.get("navegar/usuarios/nuevo", ctx -> {
      ctx.header("HX-Redirect","/usuarios/nuevo");
    });

    app.get("/usuarios/nuevo", ctx -> {
      ctx.render("templates/paginas/registrarse");
    });

  }
}
