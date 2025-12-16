package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.estadisticas.ExportarEstadisticasCSV;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.execpciones.ExportarEstadisticasException;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EstadisticasController {

  public Map<String,Object> estadisticasColecciones(Context ctx,Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();

    Long coleccionId = Long.parseLong(ctx.queryParam("coleccion")) ;
    String fechaDesdeStr = ctx.queryParam("fecha-desde");
    String fechaHastaStr = ctx.queryParam("fecha-hasta");

    if (coleccionId != null &&
        fechaDesdeStr != null && !fechaDesdeStr.isEmpty() &&
        fechaHastaStr != null && !fechaHastaStr.isEmpty()) {

      try {

        LocalDateTime desde = LocalDate.parse(fechaDesdeStr).atStartOfDay();
        LocalDateTime hasta = LocalDate.parse(fechaHastaStr).atStartOfDay();

        Coleccion coleccion = new RepoColecciones().find(coleccionId);

        model.put("resultadoEstadistico", estadistico.resultadosEstudioColeccion(coleccion,desde,hasta));
        model.put("provinciaConMasHechosReportados", estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccion, desde, hasta));

      } catch (java.time.format.DateTimeParseException e) {

        System.err.println("Error de formato de fecha: " + e.getMessage());
        model.put("error", "Las fechas no tienen un formato válido ");
      }

    } else {
      model.put("mensaje", "Por favor, complete la Categoría, Fecha Desde y Fecha Hasta para ver las estadísticas.");
    }

    return model;
  }

  public Map<String ,Object> estadisticasCategorias(Context ctx, Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();

    String categoria = ctx.queryParam("categoria");
    String fechaDesdeStr = ctx.queryParam("fecha-desde");
    String fechaHastaStr = ctx.queryParam("fecha-hasta");

    if (categoria != null && !categoria.isEmpty() &&
        fechaDesdeStr != null && !fechaDesdeStr.isEmpty() &&
        fechaHastaStr != null && !fechaHastaStr.isEmpty()) {

      try {

        LocalDateTime desde = LocalDate.parse(fechaDesdeStr).atStartOfDay();
        LocalDateTime hasta = LocalDate.parse(fechaHastaStr).atStartOfDay();

        model.put("resultadoEstadistico", estadistico.resultadosEstudioCategoria(categoria, desde, hasta));
        model.put("provinciaConMasHechosReportados", estadistico.provinciaConMasHechosReportadosDeUnaCategoria(categoria, desde, hasta));
        model.put("categoriaConMasHechosReportados", estadistico.categoriaConMasHechosReportados(desde, hasta));

      } catch (java.time.format.DateTimeParseException e) {

        System.err.println("Error de formato de fecha: " + e.getMessage());
        model.put("error", "Las fechas no tienen un formato válido ");
      }

    } else {
      model.put("mensaje", "Por favor, complete la Categoría, Fecha Desde y Fecha Hasta para ver las estadísticas.");
    }

    return model;
  }

  public Map<String, Object> estadisticasSpam(Context ctx, Map<String,Object> model) {
    Estadistico estadistico = new Estadistico();
    Long rechazosTotal = estadistico.cantidadRechazosTotal();
    Long spamTotal = estadistico.cantidadDeRechazosSpam();
    Long porcentaje = (spamTotal / rechazosTotal) * 100;

    model.put("resultadoEstadistico", estadistico.resultadosEstudioSpam());
    model.put("hechoMasSpameado",estadistico.hechoMasSpameado());
    model.put("rechazosTotal", rechazosTotal);
    model.put("rechazosSpam", spamTotal);
    model.put("porcentaje", porcentaje);

    return model;
  }

  public void exportar(Context ctx, Map<String, Object> model) {

    Estadistico estadistico = new Estadistico();
    String nombreArchivo;
    List<? extends ResultadoEstadistico> resultados;

    String desdeString = ctx.queryParam("fecha-desde");
    String hastaString = ctx.queryParam("fecha-hasta");
    String tipo = ctx.queryParam("tipoEstadistica");

    if (desdeString == null || desdeString.isBlank() ||
        hastaString == null || hastaString.isBlank() ||
        tipo == null || tipo.isBlank()) {
      ctx.result("Complete los filtros para generar las estadisticas");
      return;
    }

    LocalDateTime desde = LocalDate.parse(Objects.requireNonNull(desdeString)).atStartOfDay();
    LocalDateTime hasta = LocalDate.parse(Objects.requireNonNull(hastaString)).atStartOfDay();

    switch (tipo) {
      case "categorias":
        String categoria = ctx.queryParam("categoria");

        nombreArchivo = "categoria-"+categoria+".csv";

        resultados = estadistico.resultadosEstudioCategoria(categoria,desde,hasta);
        break;
      case "colecciones":
        Coleccion coleccion = new ColeccionesRepository().find(Long.parseLong(Objects.requireNonNull(ctx.queryParam("coleccion"))));

        nombreArchivo = "coleccion-"+coleccion.getTitulo()+".csv";

        resultados = estadistico.resultadosEstudioColeccion(coleccion,desde,hasta);
        break;
      case "spam":
        nombreArchivo = "spam.csv";

        resultados = estadistico.resultadosEstudioSpam();
        break;
      default:
        throw new ExportarEstadisticasException("No se especifica el tipo de estadistica que se desea obtener");

    }

    enviarCSVAlCliente(ctx,model,nombreArchivo, (List<ResultadoEstadistico>) resultados);

  }

  private void enviarCSVAlCliente(@NotNull Context ctx, Map<String, Object> model, String nombreArchivo, List<ResultadoEstadistico> resultados) {

    ctx.header("Content-Type", "text/csv; charset=utf-8");
    ctx.header("Content-Disposition","attachment; filename="+nombreArchivo);

    ctx.result(new ExportarEstadisticasCSV().exportar(resultados));
  }
}
