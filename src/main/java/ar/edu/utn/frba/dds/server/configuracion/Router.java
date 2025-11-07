package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SolicitudesDeEliminacionController;
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
    HechosController hechosController = new HechosController();
    SolicitudesDeEliminacionController solicitudesDeEliminacionController = new SolicitudesDeEliminacionController();

    app.before(ctx -> {
      entityManager().clear();
    });

    app.get("/", ctx ->
      ctx.render("templates/paginas/mapa/mapaPagina", homeController.show(ctx)));

    app.get("/colecciones/{id}/hechos", ctx -> {
      if(ctx.header("HX-Request") != null) {
        ctx.render("templates/paginas/mapa/hechos", coleccionesController.hechos(ctx));
      }
      else {
        Map<String, Object> model = homeController.show(ctx);
        model.putAll(coleccionesController.hechos(ctx));
        ctx.render("templates/paginas/mapa/mapaPagina", model);
      }
    });

    app.get("/hechos/nuevo", ctx -> {
      ctx.render("templates/paginas/subirHecho");
    });

    app.get("/solicitudesDeEliminacion/nueva", ctx -> {
      String paramHecho = ctx.queryParam("hecho");
      Map<String, Object> model = new HashMap<>();
      model.put("hecho", paramHecho);
      ctx.render("templates/paginas/nuevaSolicitudDeEliminacion", model);
    });

    app.get("/usuarios/nuevo", ctx -> {
      ctx.render("templates/paginas/registrarse");
    });

    app.get("/panelDeControl", ctx -> {
      ctx.render("templates/paginas/panelDeControl/panel");
    });

    app.get("/panelDeControl/colecciones", ctx -> {
      ctx.render("templates/paginas/panelDeControl/verColecciones",homeController.show(ctx));
    });

    app.get("/panelDeControl/colecciones/nueva", ctx -> {
      ctx.render("templates/paginas/panelDeControl/crearColeccion");
    });

    app.get("/panelDeControl/solicitudesDeEliminacion", ctx -> {
      ctx.render("templates/paginas/panelDeControl/solicitudesDeEliminacion");
    });

    app.post("/hechos", hechosController::subirHecho);

    app.post("/solicitudesDeEliminacion", solicitudesDeEliminacionController::subirSolicitud);
  }
}
