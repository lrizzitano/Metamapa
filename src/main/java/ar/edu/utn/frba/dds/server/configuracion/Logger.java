package ar.edu.utn.frba.dds.server.configuracion;

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
    StringBuilder sb = new StringBuilder();

    sb.append("Request: ");
    sb.append(ctx.method());
    sb.append(" ");
    sb.append(ctx.path());

    String query = ctx.queryString() != null ? ctx.queryString() : "";
    assert query != null;
    if (!query.isEmpty()) {
      sb.append("?").append(query);
    }

    sb.append(" | Status: ").append(ctx.status());
    sb.append(" | Time: ").append(executionTimeMs).append(" ms");

    logger.info(sb.toString());
  }


  public void loggearResponse(Context ctx, Float executionTimeMs) {
    StringBuilder sb = new StringBuilder();

    sb.append("Response - ");
    sb.append("Status: ").append(ctx.status()).append(", ");
    sb.append("Path: ").append(ctx.path()).append(", ");
    sb.append("Method: ").append(ctx.method()).append(", ");
    sb.append("ExecutionTimeMs: ").append(executionTimeMs);

    logger.info(sb.toString());
  }

  public void loggearExcepcion(Exception ex) {
    logger.error(ex.getMessage(), ex);
  }
}
