package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.server.exceptions.ErrorRenderizadoException;
import io.javalin.Javalin;

public class ExcepcionesHandler {

  public static void definirManejoDeException(Javalin app, Logger logger) {

    app.exception(ErrorRenderizadoException.class, (e, ctx) -> {
      logger.loggearExcepcion(e);
      ctx.status(500);
      ctx.result("Error del servidor");
    });
  }
}
