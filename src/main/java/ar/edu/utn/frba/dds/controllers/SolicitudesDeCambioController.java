package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.utils.YoutubeLinkParser;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SolicitudesDeCambioController implements WithSimplePersistenceUnit, TransactionalOps {
  public void subirSolicitud(Context ctx) {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    String provinciaStr = ctx.formParam("provincia");
    String latStr = ctx.formParam("latitud");
    String lngStr = ctx.formParam("longitud");
    String fechaStr = ctx.formParam("fecha_hecho");
    String imagen = ctx.formParam("imagen");
    String video = ctx.formParam("video");

    Set<Hecho> hechos = new HechosController().getAllHechos(new NullFiltro());


    if (
        titulo == null || titulo.isBlank() ||
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

      double latitud = Double.parseDouble(latStr);
      double longitud = Double.parseDouble(lngStr);
      Provincia provincia = Provincia.desdeNombre(provinciaStr);
      LocalDateTime fechaAcontecimiento = LocalDateTime.parse(fechaStr);

      Ubicacion ubicacion = new Ubicacion(latitud, longitud, provincia, null);
      LocalDateTime fechaCarga = LocalDateTime.now();
      Origen origen = Origen.CONTRIBUYENTE;

      Hecho hechoNUEVO = new Hecho(
          null,
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
        hechoNUEVO.setContribuyente(usuario);
      }

      hechoNUEVO.setImagen(imagen);

      if (video != null && !video.isBlank()) {
        hechoNUEVO.setVideo(YoutubeLinkParser.obtenerVideoEmbebible(video));
      }


      Hecho hechoAnterior = hechos.stream()
          .filter(hecho -> {
            return hecho.titulo().equals(titulo);
          }).findFirst().get();

      HechoDTO hechoAnteriorDTO = new HechoDTO(hechoAnterior);
      HechoDTO hechoNuevoDTO = new HechoDTO(hechoNUEVO);
    if (Objects.equals(hechoAnteriorDTO.getTitulo(), hechoNuevoDTO.getTitulo())
        && Objects.equals(hechoAnteriorDTO.getDescripcion(), hechoNuevoDTO.getDescripcion())
        && Objects.equals(hechoAnteriorDTO.getCategoria(), hechoNuevoDTO.getCategoria())
        && hechoAnteriorDTO.getLatitud() == hechoNuevoDTO.getLatitud()
        && hechoAnteriorDTO.getLongitud() == hechoNuevoDTO.getLongitud()
        && Objects.equals(hechoAnteriorDTO.getFechaAcontecimiento(), hechoNuevoDTO.getFechaAcontecimiento())
        && Objects.equals(hechoAnteriorDTO.getFechaCarga(), hechoNuevoDTO.getFechaCarga())
        && Objects.equals(hechoAnteriorDTO.getOrigen(), hechoNuevoDTO.getOrigen())
        && Objects.equals(hechoAnteriorDTO.getProvincia(), hechoNuevoDTO.getProvincia())
        && Objects.equals(hechoAnteriorDTO.getVideo(), hechoNuevoDTO.getVideo())
        && Objects.equals(hechoAnteriorDTO.getImagen(), hechoNuevoDTO.getImagen())
        && Objects.equals(hechoAnteriorDTO.getContribuyente(), hechoNuevoDTO.getContribuyente())
        && Objects.equals(hechoAnteriorDTO.getFechaAcontecimientoSinFormatear(), hechoNuevoDTO.getFechaAcontecimientoSinFormatear())
    ) {
      new Logger().info("Campos sin cambiar");
      ctx.result("Se debe cambiar por lo menos un campo");
      return;
    }

      SolicitudDeCambio solicitudDeCambio = new SolicitudDeCambio(hechoAnterior,hechoNUEVO,hechoNUEVO.contribuyente());
      SolicitudesFuenteDinamicaJPA solicitudesFuenteDinamicaJPA = new SolicitudesFuenteDinamicaJPA();

      withTransaction(() -> {
        new HechosFuenteDinamicaJPA().agregar(hechoNUEVO);
        solicitudesFuenteDinamicaJPA.nuevaSolicitud(solicitudDeCambio);
      });
      ctx.header("HX-Redirect", "/");

}

  public Map<String, Object> verSolicitudes(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    SolicitudesFuenteDinamicaJPA solicitudes = new SolicitudesFuenteDinamicaJPA();

    Set<SolicitudDeCambio> solicitudesDeCambios =  solicitudes.getPendientes();

    Set<SolicitudDeCambioDTO> solicitudesDeCambioDTOs = solicitudesDeCambios.stream().
        map(SolicitudDeCambioDTO::new).collect(Collectors.toSet());

    model.put("solicitudes",solicitudesDeCambioDTOs);
    return model;
  }

  public void resolverSolicitud(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));

    UsuarioDTO usuario = ctx.sessionAttribute("usuario");
    String nombreDeUsuario;

    if (usuario != null) {
      nombreDeUsuario = usuario.usuario();
    } else {
      ctx.result("Sesion no valida");
      return;
    }

    SolicitudesFuenteDinamicaJPA solicitudes = new SolicitudesFuenteDinamicaJPA();

    SolicitudDeCambio solicitud = solicitudes.find(id);

    Administrador admin = (Administrador) new RepoUsuarios().findByUsername(nombreDeUsuario);

    if (ctx.formParam("Aceptar") != null) {
      withTransaction(() -> {
        solicitud.aceptarCambio(admin, "");
      });
    } else {
      withTransaction(() -> {
        solicitud.rechazarCambio(admin);
      });
    }

    ctx.redirect("/panelDeControl/solicitudesDeCambio");
  }
}