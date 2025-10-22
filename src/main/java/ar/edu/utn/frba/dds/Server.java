package ar.edu.utn.frba.dds;

import io.javalin.Javalin;

public class Server {

  public static void main(String[] args) {

    Logger logger = new Logger();

    try {
      Javalin app = Javalin.create(config -> {
        config.requestLogger.http(logger::loggearRequest);
        config.router.ignoreTrailingSlashes = true; // /ruta = /ruta/
        config.router.treatMultipleSlashesAsSingleSlash = true; // ruta//x = ruta/x
        config.router.caseInsensitiveRoutes = true; // /ruta = /RUTA
      });

      app.events(event -> {
        event.serverStarting(() -> {
          logger.info("Iniciando Servidor");
        });
        event.serverStarted(() -> {
          logger.info("Servidor Iniciado");
        });
        event.serverStopping(() -> {
          logger.info("Deteniendo Servidor");
        });
        event.serverStopped(() -> {
          logger.info("Servidor Detenido");
        });
      });

      /* juanma tira tu magia de desarrollo */
      // ErrorHandler.registrarErroresGlobales(app,logger /* con loggear error */)

      Routes.routearApp(app);

      app.start();

    } catch (Exception e) {

      logger.error("Error al iniciar Servidor", e);

    }
  }
}
