package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SolicitudesDeEliminacionController implements WithSimplePersistenceUnit, TransactionalOps {
  public void subirSolicitud(Context ctx) {
    String contentType = ctx.contentType(); // puede ser "application/json" o "application/x-www-form-urlencoded"
    String hecho;
    String fundamentacion;

    if (contentType != null && contentType.contains("application/json")) {
      // viene desde la api
      Map<String, String> data = ctx.bodyAsClass(Map.class);
      hecho = data.get("hecho");
      fundamentacion = data.get("fundamentacion");
    } else {
      // Viene desde un form html
      hecho = ctx.formParam("hecho");
      fundamentacion = ctx.formParam("fundamentacion");
    }

    if (hecho == null || hecho.isBlank() ||
        fundamentacion == null || fundamentacion.isBlank()) {
      new Logger().info("Falta informacion de la solicitud de eliminacion");
      ctx.result("Falta informacion de la solicitud de eliminacion");
      return;
    }

    try {
      SolicitudDeEliminacion solicitudDeEliminacion = new SolicitudDeEliminacion(hecho, fundamentacion);

      withTransaction(() -> {
        new SolicitudesDeEliminacionJPA().nuevaSolicitud(solicitudDeEliminacion);
      });

      ctx.header("HX-Redirect", "/");

    } catch (Exception e) {
      new Logger().info("Error al crear solicitud de eliminación: " + e.getMessage());
      ctx.status(500).json(Map.of(
          "error", "Error al crear solicitud de eliminación",
          "detalle", e.getMessage()
      ));
    }
  }

  public Map<String, Object> verSolicitudes(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    SolicitudesDeEliminacionJPA solicitudes = new SolicitudesDeEliminacionJPA();

    model.put("solicitudes", solicitudes.getPendientes());
    return model;
  }

  public void resolverSolicitud(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    UsuarioDTO usuario = ctx.sessionAttribute("usuario");
    String usuarioAdmin =  usuario.usuario();

    SolicitudesDeEliminacionJPA solicitudes = new SolicitudesDeEliminacionJPA();

    SolicitudDeEliminacion solicitud = solicitudes.find(id);
    //Al no persistirse el repo de solicitudes se lo debo inyectar antes de usarlo
    solicitud.setSolicitudes(solicitudes);

    Administrador admin = (Administrador) new RepoUsuarios().findByUsername(usuarioAdmin);

    if (ctx.formParam("Aceptar") != null) {
      withTransaction(() -> {
        solicitud.aceptar(admin);
      });
    } else {
      withTransaction(() -> {
        solicitud.rechazar(admin);
      });
    }

    ctx.redirect("/panelDeControl/solicitudesDeEliminacion");
  }

}
