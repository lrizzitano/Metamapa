package ar.edu.utn.frba.dds.controllers;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.javalin.http.Context;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ColeccionesController implements WithSimplePersistenceUnit, TransactionalOps {
  public Map <String, Object> hechos(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    Map<String, Object> model = new HashMap<>();
    ColeccionesRepository colecciones = new ColeccionesRepository();
    FiltroCompuesto filtro = new FiltroCompuesto();

    // obtenemos query params
    String titulo = ctx.queryParam("titulo");
    String categoria = ctx.queryParam("categoria");
    String fechaDesdeStr = ctx.queryParam("fecha-desde");
    String fechaHastaStr = ctx.queryParam("fecha-hasta");

    if (categoria != null && !categoria.isEmpty()) {
      new Logger().info("llegue a armar el filtro categoria".concat(categoria));
      filtro.and(new FiltroCategoria(categoria));
    }

    LocalDate fechaDesde = null;
    LocalDate fechaHasta = null;

    try {
      if (fechaDesdeStr != null && !fechaDesdeStr.isEmpty()) {
        fechaDesde = LocalDate.parse(fechaDesdeStr);
        filtro.and(new FiltroFechaDesde(fechaDesde.atStartOfDay()));
      }
      if (fechaHastaStr != null && !fechaHastaStr.isEmpty()) {
        fechaHasta = LocalDate.parse(fechaHastaStr);
        filtro.and(new FiltroFechaHasta(fechaHasta.atStartOfDay()));
        new Logger().info("llegue a armar el filtro fechaHasta".concat(fechaHasta.toString()));
      }
    } catch (DateTimeParseException e) {
      ctx.status(400).result("Fechas invalidas");
      // aca capaz se podria meter un cartelito de error mas lindo
    }


    withTransaction(() -> {
      Coleccion coleccion = colecciones.find(id);
      coleccion.setSolicitudes(new SolicitudesDeEliminacionJPA());
      // coleccion.hechos(titulo, filtro).stream().map(HechoDTO::new).collect(Collectors.toSet());
      // TODO: usar este cuando pasemos a mariaDB/mySQL que tienen FullTextSearch, la base esta no tiene entonces rompe
      Set<HechoDTO> hechos = coleccion.hechos(filtro).stream().map(HechoDTO::new).collect(Collectors.toSet());
      model.put("hechos", hechos);
    });

    return model;
  }
}
