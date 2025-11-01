package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.server.configuracion.HandlebarsRender;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import ar.edu.utn.frba.dds.server.configuracion.Router;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Server {

  public static void main(String[] args) {

     Logger logger = new Logger();
  
    try {
      Javalin app = Javalin.create(config -> {

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

        config.fileRenderer(new HandlebarsRender());
        config.staticFiles.add(staticConfig -> {
          staticConfig.hostedPath = "/";
          staticConfig.directory = "src/main/resources"; // adjust path as needed
          staticConfig.location = Location.EXTERNAL;
        });
        // para dev, en prod usar:
        //config.staticFiles.add("/", Location.CLASSPATH);

        config.requestLogger.http(logger::loggearRequest);
        config.router.ignoreTrailingSlashes = true; // /ruta = /ruta/
        config.router.treatMultipleSlashesAsSingleSlash = true; // ruta//x = ruta/x
        config.router.caseInsensitiveRoutes = true; // /ruta = /RUTA
      });


      /* juanma tira tu magia de desarrollo */
      // primero definir el handler de errores por seguridad
      // ExceptionHandler.definirManejoDeException(app,logger /* con loggear error */)

      new Router().routearApp(app);

      app.start();

    } catch (Exception e) {

      logger.error("Error al iniciar Servidor", e);

    }
  }
}
