package ar.edu.utn.frba.dds.controllers;

import static java.lang.Long.parseLong;

import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import io.javalin.http.Context;

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
}
