package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.SolicitudesDeEliminacionController;
import ar.edu.utn.frba.dds.server.SetupData;
import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Autorizador;
import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol;
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
      ctx.appData(AppKeys.AUTENTICADOR).verificarFirma(ctx);
    });

    app.beforeMatched(Autorizador::validarPermisos);

    app.get("/", ctx ->
      ctx.render("templates/paginas/mapa/mapaPagina", homeController.show(ctx)), Rol.USUARIO);

    app.get("/colecciones/{id}/hechos", ctx -> {
      if(ctx.header("HX-Request") != null) {
        ctx.render("templates/paginas/mapa/hechos", coleccionesController.hechos(ctx));
      }
      else {
        Map<String, Object> model = homeController.show(ctx);
        model.putAll(coleccionesController.hechos(ctx));
        ctx.render("templates/paginas/mapa/mapaPagina", model);
      }
    }, Rol.USUARIO);

    app.get("/hechos/nuevo", ctx -> {
      ctx.render("templates/paginas/subirHecho");
    }, Rol.USUARIO);

    app.get("/solicitudesDeEliminacion/nueva", ctx -> {
      String paramHecho = ctx.queryParam("hecho");
      Map<String, Object> model = new HashMap<>();
      model.put("hecho", paramHecho);
      ctx.render("templates/paginas/nuevaSolicitudDeEliminacion", model);
    });

    app.get("/usuario/nuevo", ctx -> {
      ctx.render("templates/paginas/registrarse");
    }, Rol.USUARIO);

    app.post("/usuario", ctx -> {
      // validar que no existe otro usuario con los mismo datos
        // si existe lo redirigimos al formulario con un cartel informativo
      // sino guardarlo en la base
      // generamos el token con su rol y se lo devolvemos
      // lo redirigimos a la pantalla principal
    }, Rol.USUARIO);

    app.get("/panelDeControl", ctx -> {
      ctx.render("templates/paginas/panelDeControl/panel");
    }, Rol.ADMIN);

    app.get("/panelDeControl/colecciones", ctx -> {
      ctx.render("templates/paginas/panelDeControl/verColecciones",homeController.show(ctx));
    }, Rol.ADMIN);

    app.get("/panelDeControl/colecciones/nueva", ctx -> {
      ctx.render("templates/paginas/panelDeControl/crearColeccion");
    }, Rol.ADMIN);

    app.get("/panelDeControl/solicitudesDeEliminacion", ctx -> {
      ctx.render("templates/paginas/panelDeControl/solicitudesDeEliminacion", solicitudesDeEliminacionController.verSolicitudes(ctx));
    }, Rol.ADMIN);

    app.post("/hechos", hechosController::subirHecho, Rol.USUARIO);

    app.post("/solicitudesDeEliminacion", solicitudesDeEliminacionController::subirSolicitud, Rol.USUARIO);

    app.post("/solicitudesDeEliminacion/{id}", solicitudesDeEliminacionController::resolverSolicitud, Rol.ADMIN);

    app.post("/colecciones",coleccionesController::subirColeccion);

  }
}
