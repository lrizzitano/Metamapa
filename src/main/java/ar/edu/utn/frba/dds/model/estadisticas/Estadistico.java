package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeColeccion;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Estadistico implements WithSimplePersistenceUnit {

  public Estadistico() {}

  // Colecciones
  public List<ResultadoEstudioColeccion> resultadosEstudioColeccion(Coleccion coleccion, LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select e " +
                "from ResultadoEstudioColeccion e " + // el join lo hace igual
                "where e.fecha between :desde and :hasta " +
                "and e.coleccion = :coleccion " +
                "order by e.fecha desc",
            ResultadoEstudioColeccion.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setParameter("coleccion", coleccion)
        .getResultList();
  }

  public Provincia provinciaConMayorCantidadDeHechosReportadosDeColeccion(Coleccion coleccion, LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select r.provincia " +
                "from ResultadoEstudioColeccion e " +
                "join e.hechosPorProvincia r " +
                "where e.fecha between :desde and :hasta " +
                "and e.coleccion = :coleccion " +
                "group by r.provincia " +
                "order by sum(r.cant_hechos) desc",
            Provincia.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setParameter("coleccion", coleccion)
        .setMaxResults(1)
        .getSingleResult();
  }

  // Categoria
  public List<ResultadoEstudioCategoria> resultadosEstudioCategoria(String categoria, LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select e " +
                "from ResultadoEstudioCategoria e " +
                "where e.fecha between :desde and :hasta " +
                "and e.categoria = :categoria " +
                "order by e.fecha desc",
            ResultadoEstudioCategoria.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setParameter("categoria", categoria)
        .getResultList();
  }

  public Provincia provinciaConMasHechosReportadosDeUnaCategoria(String categoria, LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select h.provincia " +
                "from ResultadoEstudioCategoria e " +
                "join e.hechosPorProvincia h " +
                "where e.fecha between :desde and :hasta " +
                "and e.categoria = :categoria " +
                "group by h.provincia " +
                "order by sum(h.cant_hechos) desc",
            Provincia.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setParameter("categoria", categoria)
        .setMaxResults(1)
        .getSingleResult();
  }

  public String categoriaConMasHechosReportados(LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select e.categoria " +
                "from ResultadoEstudioCategoria e " +
                "where e.fecha between :desde and :hasta " +
                "group by e.categoria " +
                "order by sum(e.total_hechos) desc",
            String.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setMaxResults(1)
        .getSingleResult();
  }

  // Spam
  //public resultadosEstudioSolicitudes(LocalDateTime desde, LocalDateTime hasta) {
  //  entityManager().createQuery(
  //          "select r.cantidadSpam" +
  //              "where between(:desde,:hasta)" +
  //              " RechazosDeEliminacion r ",
  //          Long.class
  //      )
  //      .setParameter("desde", desde)
  //      .setParameter("hasta", hasta)
  //      .getSingleResult();
  //}

  // viejas

  public LocalTime horaPicoDeReporteDeUnaCategoria(String categoria, LocalDateTime fecha) {
    return entityManager().createQuery(
            "select r.hora_pico " +
                "from ResultadoEstudioCategoria r " +
                "where r.fecha = :fecha and r.categoria = :categoria",
            LocalTime.class
        )
        .setParameter("fecha", fecha)
        .setParameter("categoria", categoria)
        .getSingleResult();
  }

  public long cantidadDeSpamEnSolicitudesDeEliminacion() {
    return entityManager().createQuery(
            "select sum(r.cantidadSpam)" +
                "from RechazosDeEliminacion r ",
        Long.class
        )
        .getSingleResult();
  }
}
