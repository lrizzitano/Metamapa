package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class EstadisticasController {

  public static void estadisticasColecciones(Context ctx,Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();

    Coleccion coleccion = new ColeccionesRepository().find(Long.parseLong(Objects.requireNonNull(ctx.formParam("coleccion"))));
    LocalDateTime desde = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("desde")));
    LocalDateTime hasta = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("desde")));

    model.put("resultadoEstadistico", estadistico.resultadosEstudioColeccion(coleccion,desde,hasta));

    model.put("provinciaConMasHechosReportados", estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccion, desde, hasta));

    ctx.render("/templates/paginas/estadisticas/contenidoColecciones", model);
  }

  public static void estadisticasCategorias(Context ctx, Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();

    String categoria = ctx.formParam("categoria");
    LocalDateTime desde = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("desde")));
    LocalDateTime hasta = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("desde")));

    model.put("resultadoEstadistico", estadistico.resultadosEstudioCategoria(categoria,desde,hasta));

    model.put("provinciaConMasHechosReportados", estadistico.provinciaConMasHechosReportadosDeUnaCategoria(categoria, desde, hasta));

    model.put("categoriaConMasHechosReportados",estadistico.categoriaConMasHechosReportados(desde,hasta));

    ctx.render("/templates/paginas/estadisticas/contenidoCategorias", model);
  }
}
