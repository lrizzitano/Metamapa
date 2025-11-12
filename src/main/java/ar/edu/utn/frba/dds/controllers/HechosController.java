package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.utils.YoutubeLinkParser;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.time.LocalDateTime;

public class HechosController implements WithSimplePersistenceUnit, TransactionalOps {
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
}
