package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

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

}
