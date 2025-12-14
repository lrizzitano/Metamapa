package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.estadisticas.ExportarEstadisticasCSV;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EstadisticasController {

  public static Map<String,Object> estadisticasColecciones(Context ctx,Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();

    Long coleccionId = Long.parseLong(ctx.queryParam("coleccion")) ;
    String fechaDesdeStr = ctx.queryParam("fecha-desde");
    String fechaHastaStr = ctx.queryParam("fecha-hasta");

    if (coleccionId != null &&
        fechaDesdeStr != null && !fechaDesdeStr.isEmpty() &&
        fechaHastaStr != null && !fechaHastaStr.isEmpty()) {

      try {

        LocalDate desde = LocalDate.parse(fechaDesdeStr);
        LocalDate hasta = LocalDate.parse(fechaHastaStr);


        //model.put("resultadoEstadistico", estadistico.resultadosEstudioColeccion(coleccion,desde,hasta));
        //model.put("provinciaConMasHechosReportados", estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccion, desde, hasta));

        Coleccion coleccion = new RepoColecciones().find(coleccionId);

        ResultadoEstudioColeccion resultadoEstudioColeccion =
            new ResultadoEstudioColeccion(LocalDateTime.now().minusDays(3),coleccion
                ,Long.parseLong("20"),null);

        List<ResultadoEstudioColeccion> listaResultados = new ArrayList<>();
        listaResultados.add(resultadoEstudioColeccion);

        model.put("resultadoEstadistico", listaResultados);

      } catch (java.time.format.DateTimeParseException e) {

        System.err.println("Error de formato de fecha: " + e.getMessage());
        model.put("error", "Las fechas no tienen un formato válido ");
      }

    } else {
      model.put("mensaje", "Por favor, complete la Categoría, Fecha Desde y Fecha Hasta para ver las estadísticas.");
    }

    return model;
  }

  public static Map<String ,Object> estadisticasCategorias(Context ctx, Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();

    String categoria = ctx.queryParam("categoria");
    String fechaDesdeStr = ctx.queryParam("fecha-desde");
    String fechaHastaStr = ctx.queryParam("fecha-hasta");

    if (categoria != null && !categoria.isEmpty() &&
        fechaDesdeStr != null && !fechaDesdeStr.isEmpty() &&
        fechaHastaStr != null && !fechaHastaStr.isEmpty()) {

      try {

        LocalDate desde = LocalDate.parse(fechaDesdeStr);
        LocalDate hasta = LocalDate.parse(fechaHastaStr);


        //model.put("resultadoEstadistico", estadistico.resultadosEstudioCategoria(categoria, desde.atStartOfDay(), hasta.atStartOfDay()));
        //model.put("provinciaConMasHechosReportados", estadistico.provinciaConMasHechosReportadosDeUnaCategoria(categoria, desde.atStartOfDay(), hasta.atStartOfDay()));
        //model.put("categoriaConMasHechosReportados", estadistico.categoriaConMasHechosReportados(desde.atStartOfDay(), hasta.atStartOfDay()));

        model.put("provinciaConMasHechosReportados","Chaco");
        model.put("categoriaConMasHechosReportados","Desastre natural");

        ResultadoEstudioCategoria resultadoEstudioCategoria =
            new ResultadoEstudioCategoria(LocalDateTime.now().minusDays(3), "Milagro", 28, 22.20, null);

        List<ResultadoEstudioCategoria> listaResultados = new ArrayList<>();
        listaResultados.add(resultadoEstudioCategoria);
        listaResultados.add(resultadoEstudioCategoria);
        listaResultados.add(resultadoEstudioCategoria);

        model.put("resultadoEstadistico", listaResultados);

      } catch (java.time.format.DateTimeParseException e) {

        System.err.println("Error de formato de fecha: " + e.getMessage());
        model.put("error", "Las fechas no tienen un formato válido ");
      }

    } else {
      model.put("mensaje", "Por favor, complete la Categoría, Fecha Desde y Fecha Hasta para ver las estadísticas.");
    }

    return model;
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
