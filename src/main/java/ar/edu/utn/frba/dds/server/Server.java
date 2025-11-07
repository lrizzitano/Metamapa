package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.server.configuracion.autenticacion.FactoryAutenticador;
import ar.edu.utn.frba.dds.server.configuracion.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Server {

  public static void main(String[] args) {

     Logger logger = new Logger();
  
    try {
      Javalin app = Javalin.create(config -> {

        // Eventos
        config.events.serverStarting(() -> {
          logger.info("Iniciando Servidor");
        });
        config.events.serverStarted(() -> {
          logger.info("Servidor Iniciado");
        });
        config.events.serverStopping(() -> {
          logger.info("Deteniendo Servidor");
        });
        config.events.serverStopped(() -> {
          logger.info("Servidor Detenido");
        });

        // Renderizado
        config.fileRenderer(new HandlebarsRender());
        config.staticFiles.add(staticConfig -> {
          staticConfig.hostedPath = "/";
          staticConfig.directory = "src/main/resources"; // adjust path as needed
          staticConfig.location = Location.EXTERNAL;
        });

        // para dev, en prod usar:
        //config.staticFiles.add("/", Location.CLASSPATH);

        // Logger
        config.requestLogger.http(logger::loggearRequest);
        config.appData(AppKeys.LOGGER, logger);

        // Autenticador
        config.appData(AppKeys.AUTENTICADOR, new FactoryAutenticador().crearAutenticador());

        // Varias
        config.router.ignoreTrailingSlashes = true; // /ruta = /ruta/
        config.router.treatMultipleSlashesAsSingleSlash = true; // ruta//x = ruta/x
        config.router.caseInsensitiveRoutes = true; // /ruta = /RUTA
      });

      // primero definir el handler de errores por seguridad
      new ExcepcionesHandler().definirManejoDeExcepciones(app);

      new Router().routearApp(app);

      app.start(7023);

    } catch (Exception e) {

      logger.error("Error al iniciar Servidor", e);

    }
  }
}
