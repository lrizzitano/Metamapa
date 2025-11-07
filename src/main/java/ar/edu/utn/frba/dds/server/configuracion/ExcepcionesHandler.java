package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.server.exceptions.CrearSesionException;
import ar.edu.utn.frba.dds.server.exceptions.ErrorRenderizadoException;
import ar.edu.utn.frba.dds.server.exceptions.SesionInvalidaException;
import io.javalin.Javalin;
import io.javalin.config.Key;

public class ExcepcionesHandler {

  private final Key<Logger> logger;

  public ExcepcionesHandler() {
    this.logger = AppKeys.LOGGER;
  }

  public void definirManejoDeExcepciones(Javalin app) {

    app.exception(Exception.class, (e, ctx) -> {
      ctx.appData(logger).loggearExcepcion(e);
      ctx.status(500);
      ctx.result("Error del servidor");
    });

    app.exception(ErrorRenderizadoException.class, (e, ctx) -> {
      ctx.appData(logger).loggearExcepcion(e);
      ctx.status(500);
      ctx.result("Error del servidor");
    });

    app.exception(SesionInvalidaException.class, (e, ctx) -> {
      ctx.status(401);
      // redirigir al usuario??
    });

    app.exception(CrearSesionException.class, (e, ctx) -> {
      ctx.status(400);
      // redirigir al usuario??
    });
  }
}
