package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.EstadisticasController;
import ar.edu.utn.frba.dds.controllers.FuentesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.SolicitudesDeCambioController;
import ar.edu.utn.frba.dds.controllers.SolicitudesDeEliminacionController;
import ar.edu.utn.frba.dds.controllers.UsuarioController;
import ar.edu.utn.frba.dds.controllers.UsuarioDTO;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.server.SetupData;
import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Autorizador;
import ar.edu.utn.frba.dds.server.configuracion.autorizacion.Rol;
import ar.edu.utn.frba.dds.server.exceptions.UsuarioNoAutorizadoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Router implements WithSimplePersistenceUnit {

  public void routearApp(Javalin app) {
    new SetupData().setup();
    ColeccionesController coleccionesController = new ColeccionesController();
    HechosController hechosController = new HechosController();
    SolicitudesDeEliminacionController solicitudesDeEliminacionController = new SolicitudesDeEliminacionController();
    UsuarioController usuarioController = new UsuarioController();
    FuentesController fuentesController = new FuentesController();
    EstadisticasController estadisticasController = new EstadisticasController();
    SolicitudesDeCambioController solicitudesDeCambioController = new SolicitudesDeCambioController();

    app.before(ctx -> new Middleware().orquestarBefore(ctx));

    app.beforeMatched(Autorizador::validarPermisosDeRuta);

    app.error(404, ctx ->
        ctx.render("templates/paginas/paginaNoEncontrada"));

    app.exception(UsuarioNoAutorizadoException.class, (e, ctx) ->
        ctx.redirect("/"));

    app.get("/", ctx ->
    {
      ctx.render("templates/paginas/mapa/mapaPagina", this.modelLayout(ctx));
    }, Rol.USUARIO);

    app.get("/colecciones/{id}/hechos", ctx -> {
      if (ctx.header("HX-Request") != null) {
        ctx.render("templates/paginas/mapa/hechos", coleccionesController.hechosParaRender(ctx));
      } else {
        Map<String, Object> map = new HashMap<>();
        map.putAll(this.modelLayout(ctx));
        map.putAll(coleccionesController.hechosParaRender(ctx));
        map.put("coleccionSeleccionada", new ColeccionesRepository()
            .find(Long.parseLong(ctx.pathParam("id")))
            .getTitulo());
        ctx.render("templates/paginas/mapa/mapaPagina", map);
      }
    }, Rol.USUARIO);

    app.get("/hechos/nuevo", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);
      ctx.render("templates/paginas/subirHecho", model);
    }, Rol.USUARIO);

    app.get("/hechos/{titulo}", ctx -> {
      Map<String, Object> map = new HashMap<>();
      map.putAll(this.modelLayout(ctx));
      map.put("coleccionSeleccionada", "Hecho particular");
      map.put("hechos", hechosController.findHecho(ctx.pathParam("titulo")));
      ctx.render("templates/paginas/mapa/mapaPagina", map);
    }, Rol.ADMINISTRADOR);

    app.get("/solicitudesDeEliminacion/nueva", ctx -> {
      String paramHecho = ctx.queryParam("hecho");
      Map<String, Object> model = new HashMap<>();
      model.put("hecho", paramHecho);
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/nuevaSolicitudDeEliminacion", model);
    });

    app.get("/usuarios/nuevo", ctx -> {
      ctx.render("templates/paginas/registrarse");
    }, Rol.USUARIO);

    app.post("/usuario", usuarioController::registrarUsuario, Rol.USUARIO);

    app.post("/login", usuarioController::iniciarSesion);

    app.get("/usuarios/sinSesion", usuarioController::cerrarSesion);

    app.get("/panelDeControl", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);
      ctx.render("templates/paginas/panelDeControl/panelDeControlLayout", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/colecciones", ctx -> {
      Map<String, Object> model = coleccionesController.colecciones(ctx);
      model = mantenerSesion(ctx, model);

      ctx.render("templates/paginas/panelDeControl/verColecciones", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/fuentes", ctx -> {
      Map<String, Object> model = fuentesController.fuentes();
      model = mantenerSesion(ctx, model);

      ctx.render("templates/paginas/panelDeControl/verFuentes", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/colecciones/nueva", ctx -> {
      Map<String, Object> model = fuentesController.fuentes();
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/panelDeControl/crearColeccion", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/fuentes/nuevaProxy", ctx -> {
      Map<String, Object> model = new HashMap<>();
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/panelDeControl/crearFuenteProxy", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/fuentes/nuevaEstatica", ctx -> {
      Map<String, Object> model = new HashMap<>();
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/panelDeControl/crearFuenteEstatica", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/solicitudesDeEliminacion", ctx -> {
      Map<String, Object> model = solicitudesDeEliminacionController.verSolicitudes(ctx);
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/panelDeControl/solicitudesDeEliminacion", model);
    }, Rol.ADMINISTRADOR);

    app.get("/panelDeControl/solicitudesDeCambio", ctx -> {
      Map<String, Object> model = solicitudesDeCambioController.verSolicitudes(ctx);
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/panelDeControl/solicitudesDeCambio", model);
    }, Rol.ADMINISTRADOR);

    app.post("/hechos", hechosController::subirHecho, Rol.USUARIO);

    app.post("/solicitudesDeEliminacion", solicitudesDeEliminacionController::subirSolicitud, Rol.USUARIO);

    app.post("/solicitudesDeEliminacion/{id}", solicitudesDeEliminacionController::resolverSolicitud, Rol.ADMINISTRADOR);

    app.post("/colecciones", coleccionesController::subirColeccion, Rol.ADMINISTRADOR);

    app.post("/fuentes", fuentesController::subirFuente, Rol.ADMINISTRADOR);

    app.post("/colecciones/{id}", coleccionesController::modificarColeccion, Rol.ADMINISTRADOR);

    app.delete("/colecciones/{id}", coleccionesController::eliminarColeccion, Rol.ADMINISTRADOR);

    app.get("/usuarios/{username}/perfil", ctx -> {
      Map<String, Object> model = usuarioController.usuario(ctx);
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/perfilUsuario", model);
    }, Rol.USUARIO);

    app.get("/panelDeControl/colecciones/{id}", ctx -> {
      Map<String, Object> model = coleccionesController.encontrarColeccionPorId(ctx);
      model = mantenerSesion(ctx, model);
      ctx.render("templates/paginas/panelDeControl/detalleColeccion", model);
    }, Rol.ADMINISTRADOR);

    app.post("/usuarios/{username}", usuarioController::editarPerfil, Rol.USUARIO);

    app.get("/estadisticas", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);
      ctx.render("/templates/paginas/estadisticas/sinEstadisticas", model);
    }, Rol.USUARIO);

    app.get("/estadisticas/categorias", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);
      if (ctx.header("HX-Request") != null) {
        ctx.render("templates/paginas/estadisticas/resultadosCategorias", estadisticasController.estadisticasCategorias(ctx, model));
      } else {
        ctx.render("/templates/paginas/estadisticas/categorias", model);
      }
    }, Rol.USUARIO);

    app.get("/estadisticas/colecciones", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);
      if (ctx.header("HX-Request") != null) {
        ctx.render("/templates/paginas/estadisticas/resultadosColecciones", estadisticasController.estadisticasColecciones(ctx, model));
      } else {
        model = coleccionesController.colecciones(ctx);
        model = mantenerSesion(ctx, model);
        ctx.render("/templates/paginas/estadisticas/colecciones", model);
      }
    }, Rol.USUARIO);

    app.get("/estadisticas/spam", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);

      ctx.render("/templates/paginas/estadisticas/spam", estadisticasController.estadisticasSpam(ctx, model));

    }, Rol.ADMINISTRADOR);

    app.get("/exportar", ctx -> {
      Map<String, Object> model = mantenerSesion(ctx, null);
      estadisticasController.exportar(ctx, model);
    });

    app.get("/solicitudesDeCambio/nueva", ctx -> {
      String paramHecho = ctx.queryParam("hecho");
      Map<String, Object> model = new HashMap<>();
      model = hechosController.findHecho(paramHecho);
      model = mantenerSesion(ctx, model);
      ctx.render("/templates/paginas/nuevaSolicitudDeCambio", model);
    });

    app.post("/solicitudesDeCambio", solicitudesDeCambioController::subirSolicitud, Rol.USUARIO);

    app.post("/solicitudesDeCambio/{id}", solicitudesDeCambioController::resolverSolicitud, Rol.ADMINISTRADOR);

    // Metamapa API
    app.get("/api/colecciones/{id}/hechos", coleccionesController::hechosAPI);
    app.get("/api/hechos", hechosController::todosLosHechos);
    app.post("/api/solicitudes", solicitudesDeEliminacionController::subirSolicitud);
  }

  public static Map<String, Object> mantenerSesion(Context ctx, Map<String, Object> model) {

    if (model == null) {
      model = Objects.requireNonNull(ctx.attribute(AppKeys.MODEL));
    } else {
      model.putAll(Objects.requireNonNull(ctx.attribute(AppKeys.MODEL)));
    }

    UsuarioDTO usuario = ctx.sessionAttribute("usuario");
    model.put("usuario", usuario);
    return model;
  }

  private Map<String, Object> modelLayout(Context ctx) {
    ColeccionesController coleccionesController = new ColeccionesController();
    Map<String, Object> model = coleccionesController.colecciones(ctx);
    model = mantenerSesion(ctx, model);
    return model;
  }
}
