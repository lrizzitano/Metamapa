package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.utils.JsonConverter;
import ar.edu.utn.frba.dds.controllers.utils.YoutubeLinkParser;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HechosController implements WithSimplePersistenceUnit, TransactionalOps {
  static FiltroCompuesto filtroDesdeRequest(Context ctx) {
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

    return filtro;
  }

  public void subirHecho(Context ctx) {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    String provinciaStr = ctx.formParam("provincia");
    String latStr = ctx.formParam("latitud");
    String lngStr = ctx.formParam("longitud");
    String fechaStr = ctx.formParam("fecha_hecho");
    String imagen = ctx.formParam("imagen");
    String video = ctx.formParam("video");

    // validaciones de lo obligatorio (video es opcional)
    if (titulo == null || titulo.isBlank() ||
        descripcion == null || descripcion.isBlank() ||
        categoria == null || categoria.isBlank() ||
        provinciaStr == null || provinciaStr.isBlank() ||
        latStr == null || latStr.isBlank() ||
        lngStr == null || lngStr.isBlank() ||
        fechaStr == null || fechaStr.isBlank() ||
        imagen == null || imagen.isBlank()) {
      new Logger().info("Campos incompletos");
      ctx.result("Campos incompletos");
      return;
    }

    try {
      double latitud = Double.parseDouble(latStr);
      double longitud = Double.parseDouble(lngStr);
      Provincia provincia = Provincia.valueOf(provinciaStr);
      LocalDateTime fechaAcontecimiento = LocalDateTime.parse(fechaStr);

      Ubicacion ubicacion = new Ubicacion(latitud, longitud, provincia, null);
      LocalDateTime fechaCarga = LocalDateTime.now();
      Origen origen = Origen.CONTRIBUYENTE;

      Hecho hecho = new Hecho(
          null,               // id
          titulo,
          descripcion,
          categoria,
          ubicacion,
          fechaCarga,
          fechaAcontecimiento,
          origen
      );

      UsuarioDTO usuarioDTO = ctx.sessionAttribute("usuario");

      if (usuarioDTO != null) {
        String username = usuarioDTO.usuario();
        Usuario usuario = new RepoUsuarios().findByUsername(username);
        hecho.setContribuyente(usuario);
      }

      hecho.setImagen(imagen);

      if (video != null && !video.isBlank()) {
        hecho.setVideo(YoutubeLinkParser.obtenerVideoEmbebible(video));
      }

      withTransaction(() -> {
        new HechosFuenteDinamicaJPA().agregar(hecho);
      });
      ctx.header("HX-Redirect", "/");


    } catch (Exception e) {
      new Logger().info("Error al crear hecho: " + e.getMessage());
      ctx.result("Error al crear hecho");
    }
  }

  public Set<Hecho> getAllHechos(Filtro filtro) {
    Set<Hecho> hechos;
    Set<Fuente> fuentes = new FuentesRepositoryJPA().obtenerFuentes();
    hechos = fuentes.stream()
        .flatMap((f) -> {
          try {
            return f.obtenerHechos(filtro).stream();
          } catch (Exception e) {
            return Stream.empty();
          }
        })
        .collect(Collectors.toSet());
    return hechos;
  }

  public void todosLosHechos(Context ctx) {
    FiltroCompuesto filtro = HechosController.filtroDesdeRequest(ctx);
    Set<Hecho> hechos = getAllHechos(filtro);
    ctx.json(new JsonConverter().armarConvertor().toJson(hechos));
  }

  public Map<String, Object> findHecho(String titulo) {
    FuentesRepositoryJPA repoFuentes = new FuentesRepositoryJPA();
    Map<String, Object> model = new HashMap<>();

    Set<Hecho> hechos = getAllHechos(new NullFiltro());
    Hecho hechoEncontrado = hechos.stream()
        .filter(hecho -> {
          return hecho.titulo().equals(titulo);
        }).findFirst().get();

    model.put("hechoEncontrado", new HechoDTO(hechoEncontrado));
    return model;
  }
}
