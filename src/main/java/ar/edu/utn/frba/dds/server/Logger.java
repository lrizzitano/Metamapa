package ar.edu.utn.frba.dds;

import io.javalin.http.Context;
import org.slf4j.LoggerFactory;

public class Logger {

  private final org.slf4j.Logger logger;

  public Logger() {
    logger = LoggerFactory.getLogger(Logger.class);
  }

  public void info(String mensaje) {
    logger.info(mensaje);
  }

  public void error(String mensaje, Exception error) {
    if (error != null) {
      logger.error(mensaje, error);
    }
      logger.error(mensaje);
  }

  // ctx = contexto de Javalin, executionTimeMs = tiempo en milisegundos
  public void loggearRequest(Context ctx, Float executionTimeMs) {
    String method = ctx.method().toString();
    String path = ctx.path();
    String query = ctx.queryString() != null ? ctx.queryString() : "";
    int status = ctx.status().getCode();

    String logMessage = String.format(
        "Request: %s %s?%s | Status: %d | Time: %f ms",
        method, path, query, status, executionTimeMs
    );

    logger.info(logMessage);
  }

  public void loggearResponse(Context ctx, Float executionTimeMs) {}

  public void loggearError() {}
}
