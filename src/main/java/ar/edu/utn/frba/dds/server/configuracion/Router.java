package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SolicitudesDeEliminacionController;
import ar.edu.utn.frba.dds.server.SetupData;
import ar.edu.utn.frba.dds.server.autenticacion.Autenticador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import io.javalin.config.Key;

import java.util.HashMap;
import java.util.Map;

public class Router implements WithSimplePersistenceUnit {

  public void routearApp(Javalin app) {
    new SetupData().setup();
    Key<Autenticador> autenticador = AppKeys.AUTENTICADOR;
    HomeController homeController = new HomeController();
    ColeccionesController coleccionesController = new ColeccionesController();
    HechosController hechosController = new HechosController();
    SolicitudesDeEliminacionController solicitudesDeEliminacionController = new SolicitudesDeEliminacionController();

    app.before(ctx -> {
      entityManager().clear();
    });

    app.beforeMatched(ctx -> ctx.appData(autenticador).verificarFirma(ctx));

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

    app.get("/usuario/nuevo", ctx -> {
      ctx.render("templates/paginas/registrarse");
    });

    app.post("/usuario", ctx -> {
      // validar que no existe otro usuario con los mismo datos
        // si existe lo redirigimos al formulario con un cartel informativo
      // sino guardarlo en la base
      // generamos el token con su rol y se lo devolvemos
      // lo redirigimos a la pantalla principal
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
      ctx.render("templates/paginas/panelDeControl/solicitudesDeEliminacion", solicitudesDeEliminacionController.verSolicitudes(ctx));
    });

    app.post("/hechos", hechosController::subirHecho);

    app.post("/solicitudesDeEliminacion", solicitudesDeEliminacionController::subirSolicitud);

    app.post("/solicitudesDeEliminacion/{id}", solicitudesDeEliminacionController::resolverSolicitud);

  }
}
