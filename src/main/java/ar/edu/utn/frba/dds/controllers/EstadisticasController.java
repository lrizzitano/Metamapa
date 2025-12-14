package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.estadisticas.ExportarEstadisticasCSV;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultadosCSV;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.server.exceptions.ArchivoNoEliminadoException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.List;
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

  public static void exportarCategoria(@NotNull Context ctx, Map<String, Object> model) {
    Estadistico estadistico = new Estadistico();

    String categoria = ctx.formParam("categoria");
    LocalDateTime desde = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("desde")));
    LocalDateTime hasta = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("hasta")));

    String nombreArchivo = "categoria-"+categoria+"-"+ctx.formParam("desde")+"-"+ctx.formParam("hasta")+".csv";

    List<? extends ResultadoEstadistico> resultados = estadistico.resultadosEstudioCategoria(categoria,desde,hasta);

    enviarCSVAlCliente(ctx,model,nombreArchivo, (List<ResultadoEstadistico>) resultados);
  }

  public static void exportarColecion(@NotNull Context ctx, Map<String, Object> model) {
    Estadistico estadistico = new Estadistico();

    Coleccion coleccion = new ColeccionesRepository().find(Long.parseLong(Objects.requireNonNull(ctx.formParam("coleccion"))));
    LocalDateTime desde = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("desde")));
    LocalDateTime hasta = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("hasta")));

    String nombreArchivo = "coleccion-"+coleccion.getTitulo()+"-"+ctx.formParam("desde")+"-"+ctx.formParam("hasta")+".csv";

    List<? extends ResultadoEstadistico> resultados = estadistico.resultadosEstudioColeccion(coleccion,desde,hasta);

    enviarCSVAlCliente(ctx,model,nombreArchivo, (List<ResultadoEstadistico>) resultados);
  }

  public static void exportarSpam(@NotNull Context ctx, Map<String, Object> model) {
  }

  public static void enviarCSVAlCliente(@NotNull Context ctx, Map<String, Object> model, String nombreArchivo, List<ResultadoEstadistico> resultados) {

    ctx.header("Content-Type", "text/csv; charset=utf-8");
    ctx.header("Content-Disposition","attachment; filename="+nombreArchivo);

    ctx.result(new ExportarEstadisticasCSV().exportar(resultados));
  }
}
