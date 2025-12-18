package ar.edu.utn.frba.dds.controllers;

import static ar.edu.utn.frba.dds.server.configuracion.Router.mantenerSesion;

import ar.edu.utn.frba.dds.controllers.utils.JsonConverter;
import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Agregador;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensosRepository;
import ar.edu.utn.frba.dds.model.hechos.consenso.TipoConsenso;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.javalin.http.Context;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ColeccionesController implements WithSimplePersistenceUnit, TransactionalOps {

  public  Map <String, Object> encontrarColeccionPorId(Context ctx){
    Map<String, Object> model = new HashMap<>();
    FuentesController fuentesController = new FuentesController();

    Long id = Long.parseLong(ctx.pathParam("id"));

    Coleccion coleccion = new RepoColecciones().find(id);

    ColeccionDTO coleccionDTO = new ColeccionDTO(coleccion);

    model.put("coleccion", coleccionDTO);

    Set<FuenteDTO> todasLasFuentes = (Set<FuenteDTO>) fuentesController.fuentes().get("fuentes");

    todasLasFuentes = todasLasFuentes.stream().filter(f -> coleccionDTO
        .fuentes()
        .stream()
        .noneMatch(fuenteColeccion -> fuenteColeccion.id().equals(f.id()))).collect(Collectors.toSet());

    // solo le paso las que no pertecen ya a esta coleccion
    model.put("fuentes", todasLasFuentes);

    return model;
  }

  public Map <String, Object> colecciones(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    Set<ColeccionDTO> colecciones = new ColeccionesRepository().findAll()
        .stream().map(ColeccionDTO::new).collect(Collectors.toSet());

    model.put("colecciones", colecciones);
    return model;
  }

  Set<Hecho> hechos;

  public Set<Hecho> obtenerHechosColeccion(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    ColeccionesRepository colecciones = new ColeccionesRepository();
    FiltroCompuesto filtro = HechosController.filtroDesdeRequest(ctx);
    String titulo = ctx.queryParam("titulo");
    String paramConsenso = ctx.queryParam("consenso");
    boolean consensuado = paramConsenso != null && paramConsenso.equals("on");

    Coleccion coleccion = colecciones.find(id);
    coleccion.setSolicitudes(new SolicitudesDeEliminacionJPA());

    boolean busquedaVacia = titulo == null || titulo.isEmpty();

    return consensuado
        ? busquedaVacia
          ? coleccion.hechosConsensuados(filtro)
          : coleccion.hechosConsensuados(titulo, filtro)
        : busquedaVacia
          ? coleccion.hechos(filtro)
          : coleccion.hechos(titulo, filtro);
  }

  public Map <String, Object> hechosParaRender(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    model.put("hechos", obtenerHechosColeccion(ctx).stream().map(HechoDTO::new).collect(Collectors.toSet()));
    model = mantenerSesion(ctx,model);
    return model;
  }

  public void hechosAPI(Context ctx) {
    hechos = obtenerHechosColeccion(ctx);
    ctx.json(new JsonConverter().armarConvertor().toJson(hechos));
  }

  public void subirColeccion(Context ctx){
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    String fechaAnterior = ctx.formParam("fechaAnterior");
    String fechaPosterior = ctx.formParam("fechaPosterior");
    List<Long> idfuentes = ctx.formParams("fuente").stream().map(Long::parseLong).toList();
    String consenso = ctx.formParam("consenso");

    if (titulo == null || titulo.isBlank()
    || descripcion == null || descripcion.isBlank()
    || consenso == null || consenso.isBlank()) {
      new Logger().info("Faltan campos por completar");
      ctx.result("Campos incompletos");
      return;
    }


    try{
      //Consenso
      Consenso consensoNuevo = obtenerConsenso(consenso);
      new Logger().info("Nuevo consenso seteado" + consensoNuevo);

      //RepoSolicitudes de eliminacion
      SolicitudesDeEliminacionJPA repoSolicitudesDeEliminacion = new SolicitudesDeEliminacionJPA();

      //Fuente
      Fuente fuenteNueva = getFuente(idfuentes);


      //Filtro Criterio pertenencia
      FiltroCompuesto criterioPertenencia = new FiltroCompuesto();


      if( categoria != null || !categoria.isEmpty() ||
           fechaAnterior != null || !fechaAnterior.isEmpty() ||
          fechaPosterior != null || !fechaPosterior.isEmpty()){


        if (!categoria.isEmpty()) {
          criterioPertenencia.and(new FiltroCategoria(categoria));

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

      if(criterioPertenencia.filtros().isEmpty()){
        criterioPertenencia.and(new NullFiltro());
      }


      Coleccion coleccion =
          new Coleccion(titulo,descripcion,criterioPertenencia,fuenteNueva,consensoNuevo,repoSolicitudesDeEliminacion);


      withTransaction(() -> {
        new RepoColecciones().save(coleccion);
      });

      ctx.header("HX-Redirect", "/panelDeControl/colecciones");


    } catch (Exception e) {
      new Logger().info("Error al crear la coleccion: " + e.getMessage());
      ctx.result("Error al crear coleccion");
    }

  }

  public void modificarColeccion(Context ctx){

    try{
      Long id = Long.parseLong(ctx.pathParam("id"));
      String consenso = ctx.formParam("consenso");
      List<Long> idfuentes = ctx.formParams("fuente").stream().map(Long::parseLong).toList();


      RepoColecciones repoColecciones = new RepoColecciones();
      Coleccion coleccion = repoColecciones.find(id);

      if(consenso != null )
      {
        Consenso nuevoConsenso = obtenerConsenso(consenso);
        coleccion.setCriterioConsenso(nuevoConsenso);
        new Logger().info("Nuevo consenso seteado modificado a" + nuevoConsenso);
      }

      Fuente fuenteNueva = getFuente(idfuentes);

      coleccion.setFuente(fuenteNueva);


      withTransaction(() -> {
        repoColecciones.update(coleccion);
      });

      ctx.header("HX-Redirect", "/panelDeControl/colecciones/");
      ctx.status(204);
    }
    catch (Exception e) {
      new Logger().info("Error al Mofidicar la coleccion: " + e.getMessage());
      ctx.result("Error al Modificar la coleccion");
    }

  }

  public Consenso obtenerConsenso(String consenso) {

    TipoConsenso tipoConsenso = switch (consenso) {
      case "Absoluto" -> TipoConsenso.ABSOLUTO;
      case "Mayoria Simple" -> TipoConsenso.MAYORIA_SIMPLE;
      case "Multiples Menciones" -> TipoConsenso.MULTIPLES_MENCIONES;
      default -> TipoConsenso.NULO;
    };

    return new ConsensosRepository().getConsenso(tipoConsenso);
  }

  public Fuente obtenerfuente(Long id ){

    FuentesRepositoryJPA repoFuentes = new FuentesRepositoryJPA();

    Fuente fuente;

    fuente = repoFuentes.find(id);

    return fuente;
  }

  public void eliminarColeccion(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));

    RepoColecciones repoColecciones = new RepoColecciones();

    withTransaction(() -> {
      Coleccion coleccion = repoColecciones.find(id);
      new Estadistico().eliminarEstadisticasDeColeccion(coleccion);
      repoColecciones.delete(coleccion);
    });

    ctx.header("HX-Redirect", "/panelDeControl/colecciones");
    ctx.status(204);
  }

  private Fuente getFuente(List<Long> idfuentes) {
    Fuente fuenteNueva;
    FuentesRepositoryJPA fuentesRepository = new FuentesRepositoryJPA();

    if(idfuentes.size()>1){
      Set<Fuente> fuentes = idfuentes.stream().map(fuentesRepository::find).collect(Collectors.toSet());
      Agregador nuevoAgregador = new Agregador(fuentes, LocalDateTime.now().plusDays(1), Duration.ofDays(1));
      nuevoAgregador.actualizar();

      withTransaction(()->{
        fuentesRepository.agregarFuente(nuevoAgregador);
      });

      fuenteNueva = nuevoAgregador;
    }
    else{
      fuenteNueva = obtenerfuente(idfuentes.get(0));
      new Logger().info("Neuva fuente seteada" + fuenteNueva);
    }
    return fuenteNueva;
  }

}
