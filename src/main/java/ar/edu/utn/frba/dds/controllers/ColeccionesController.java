package ar.edu.utn.frba.dds.controllers;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.demo.FuenteDemo;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.FuenteMetaMapa;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoConsensoAbsoluto;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoMayoriaSimple;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoMultiplesMenciones;
import ar.edu.utn.frba.dds.model.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryMemoria;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.javalin.http.Context;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDate;
import net.bytebuddy.asm.Advice;
import java.nio.file.Files;
import java.nio.file.Path;
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

  public void subirColeccion(Context ctx){
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    String fechaAnterior = ctx.formParam("fechaAnterior");
    String fechaPosterior = ctx.formParam("fechaPosterior");
    String fuente = ctx.formParam("fuente");
    String consenso = ctx.formParam("consenso");

    if (titulo == null || titulo.isBlank()
    || descripcion == null || descripcion.isBlank()
    || fuente == null || fuente.isBlank()
    || consenso == null || consenso.isBlank()) {
      new Logger().info("Faltan campos por completar");
      ctx.redirect("/panelDeControl/colecciones/nueva");
      return;
    }


    try{
      //Consenso

      Consenso consensoNuevo;

      if(consenso.equals("sinConsenso")){
        consensoNuevo = new ConsensoNull();
      }
      else{

        LocalDateTime proximaActualizacion = LocalDateTime.now().plusDays(1);
        FuentesRepositoryJPA repoFuentes = new FuentesRepositoryJPA();
        AlgoritmoConsenso algoritmo = new AlgoritmoConsensoAbsoluto();
        if(consenso.equals("absoluto")){
          algoritmo = new AlgoritmoConsensoAbsoluto();
        }
        if(consenso.equals("mayoriaSimple")){
          algoritmo = new AlgoritmoMayoriaSimple();
        }
        if(consenso.equals("multiplesMenciones")){
          algoritmo = new AlgoritmoMultiplesMenciones();
        }

        consensoNuevo = new Consenso(algoritmo,proximaActualizacion,repoFuentes);

      }

      //RepoSolicitudes de eliminacion
      SolicitudesDeEliminacionJPA repoSolicitudesDeEliminacion = new SolicitudesDeEliminacionJPA();

      //Fuente
      Fuente fuenteNueva = FuenteDinamica.instance();
      if(fuente.equals("dinamica")){
        fuenteNueva = FuenteDinamica.instance();
      }
      if(fuente.equals("metamapa")){ //TODO: que la url llegue con el form
        fuenteNueva = new FuenteMetaMapa("/url");
      }
      if(fuente.equals("estatica")){ //TODO: que el archivo llegue en el form. Podria haber muchos?
        Path tempFile = Files.createTempFile("hechos", ".csv");
        fuenteNueva = new FuenteEstatica(tempFile.toString());
      }
      if(fuente.equals("proxy")){ //TODO: Q onda esta fuente ayuda
        fuenteNueva = new FuenteDemo();
      }

      //Filtro Criterio pertenencia
      FiltroCompuesto criterioPertenencia = new FiltroCompuesto();

      new Logger().info("VINO " + categoria);

      if( categoria != null || !categoria.isEmpty() ||
           fechaAnterior != null || !fechaAnterior.isEmpty() ||
          fechaPosterior != null || !fechaPosterior.isEmpty()){

        new Logger().info("LLEGANDO " + categoria);
        if (categoria != null && !categoria.isEmpty()) {
          criterioPertenencia.and(new FiltroCategoria(categoria));

          new Logger().info("ENTRE " + categoria);
          new Logger().info("EFCHA" + fechaAnterior);
        }
        //Error en las fechas
        try {
          if (fechaAnterior != null && !fechaAnterior.isEmpty()) {
            LocalDate fechaAnteriorNueva = LocalDate.parse(fechaAnterior);
            criterioPertenencia.and(new FiltroFechaDesde(fechaAnteriorNueva.atStartOfDay()));
          }

          if (fechaPosterior != null && !fechaPosterior.isEmpty()) {
            LocalDate fechaPosteriorNueva = LocalDate.parse(fechaPosterior);
            criterioPertenencia.and(new FiltroFechaHasta(fechaPosteriorNueva.atStartOfDay()));
          }
        }catch (DateTimeParseException e) {
          new Logger().info("Fechas invalidas nashe");
        }

      }
      else{
        criterioPertenencia.and(new NullFiltro());
      }

      Coleccion coleccion =
          new Coleccion(titulo,descripcion,criterioPertenencia,fuenteNueva,consensoNuevo,repoSolicitudesDeEliminacion);

      withTransaction(() -> {
        new RepoColecciones().save(coleccion);
      });

      ctx.redirect("/panelDeControl");


    } catch (Exception e) {
      new Logger().info("Error al crear la coleccion: " + e.getMessage());
      ctx.redirect("/panelDeControl/colecciones/nueva");
    }

  }
}
