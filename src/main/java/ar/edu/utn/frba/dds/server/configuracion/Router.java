package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.SolicitudesDeEliminacionController;
import ar.edu.utn.frba.dds.controllers.UsuarioController;
import ar.edu.utn.frba.dds.server.SetupData;
import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Autorizador;
import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class Router implements WithSimplePersistenceUnit {

  public void routearApp(Javalin app) {
    new SetupData().setup();
    ColeccionesController coleccionesController = new ColeccionesController();
    HechosController hechosController = new HechosController();
    SolicitudesDeEliminacionController solicitudesDeEliminacionController = new SolicitudesDeEliminacionController();
    UsuarioController usuarioController = new UsuarioController();

    app.before(ctx -> new Middleware().orquestarBefore(ctx));

    app.beforeMatched(Autorizador::validarPermisos);

    app.get("/", ctx ->
      {
        ctx.render("templates/paginas/mapa/mapaPagina", this.modelLayout(ctx));
      }, Rol.USUARIO);

    app.get("/colecciones/{id}/hechos", ctx -> {
      if(ctx.header("HX-Request") != null) {
        ctx.render("templates/paginas/mapa/hechos", coleccionesController.hechos(ctx));
      }
      else {
        ctx.render("templates/paginas/mapa/mapaPagina", this.modelLayout(ctx));
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

    app.get("/usuarios/nuevo", ctx -> {
      ctx.render("templates/paginas/registrarse");
    }, Rol.USUARIO);

    app.post("/usuario", ctx -> {
      usuarioController.registrarUsuario(ctx);
      ctx.redirect("/");
    }, Rol.USUARIO);

    app.post("/login", ctx -> {
      usuarioController.iniciarSesion(ctx);
      ctx.redirect("/");
    });

    app.get("/panelDeControl", ctx -> {
      ctx.render("templates/paginas/panelDeControl/panel");
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/colecciones", ctx -> {
      Map<String,Object> model = coleccionesController.colecciones(ctx);

      model.putAll(coleccionesController.fuentes());

      ctx.render("templates/paginas/panelDeControl/verColecciones",model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/colecciones/nueva", ctx -> {
      ctx.render("templates/paginas/panelDeControl/crearColeccion",coleccionesController.fuentes());
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/solicitudesDeEliminacion", ctx -> {
      ctx.render("templates/paginas/panelDeControl/solicitudesDeEliminacion", solicitudesDeEliminacionController.verSolicitudes(ctx));
    }, Rol.ADMINISTRADOR);

    app.post("/hechos", hechosController::subirHecho, Rol.USUARIO);

    app.post("/solicitudesDeEliminacion", solicitudesDeEliminacionController::subirSolicitud, Rol.USUARIO);

    app.post("/solicitudesDeEliminacion/{id}", solicitudesDeEliminacionController::resolverSolicitud, Rol.ADMINISTRADOR);

    app.post("/colecciones",coleccionesController::subirColeccion,Rol.ADMINISTRADOR);

    app.post("/colecciones/{id}",coleccionesController::modificarColeccion,Rol.ADMINISTRADOR);

    app.delete("/colecciones/{id}",coleccionesController::eliminarColeccion,Rol.ADMINISTRADOR);

  }

  private Map<String, Object> modelLayout(Context ctx)
  {
    ColeccionesController coleccionesController = new ColeccionesController();
    Map<String, Object> model = coleccionesController.colecciones(ctx);
    model.putAll(ctx.attribute(AppKeys.MODEL)); // Siempre va a existir porque el model se prepara en el middleware
    return model;
  }
}
