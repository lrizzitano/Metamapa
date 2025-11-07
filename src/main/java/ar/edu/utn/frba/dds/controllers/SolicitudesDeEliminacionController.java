package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;

public class SolicitudesDeEliminacionController implements WithSimplePersistenceUnit, TransactionalOps {
  public void subirSolicitud(Context ctx) {
    String hecho = ctx.formParam("hecho");
    String fundamentacion = ctx.formParam("fundamentacion");

    if (hecho == null || hecho.isBlank() ||
        fundamentacion == null || fundamentacion.isBlank()) {
      new Logger().info("falta informacion de la solicitud de eliminacion");
      ctx.redirect("/solicitudesDeEliminacion/nueva?hecho=" + hecho);
      return;
    }

    try {
      SolicitudDeEliminacion solicitudDeEliminacion = new SolicitudDeEliminacion(hecho, fundamentacion);

      withTransaction(() -> {
        new SolicitudesDeEliminacionJPA().nuevaSolicitud(solicitudDeEliminacion);
      });

      ctx.redirect("/");
    } catch (Exception e) {
      new Logger().info("Error al crear solicitud de eliminacion: " + e.getMessage());
      ctx.redirect("/solicitudesDeEliminacion/nueva?hecho=" + hecho);
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
    SolicitudesDeEliminacionJPA solicitudes = new SolicitudesDeEliminacionJPA();

    SolicitudDeEliminacion solicitud = solicitudes.find(id);
    //Al no persistirse el repo de solicitudes se lo debo inyectar antes de usarlo
    solicitud.setSolicitudes(solicitudes);

    //TODO Traer la data del admin cuando resolvamos las sesiones
    Administrador admin = new Administrador("Ad", "Ministrador", 16);

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
