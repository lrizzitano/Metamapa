package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import io.javalin.http.Context;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ColeccionesController implements WithSimplePersistenceUnit, TransactionalOps {
  public Map <String, Object> hechos(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    Map<String, Object> model = new HashMap<>();
    ColeccionesRepository colecciones = new ColeccionesRepository();

    withTransaction(() -> {
      Coleccion coleccion = colecciones.find(id);
      coleccion.setSolicitudes(new SolicitudesDeEliminacionJPA());
      Set<HechoDTO> hechos = coleccion.hechos(new NullFiltro()).stream().map(HechoDTO::new).collect(Collectors.toSet());
      model.put("hechos", hechos);
    });

    return model;
  }
}
