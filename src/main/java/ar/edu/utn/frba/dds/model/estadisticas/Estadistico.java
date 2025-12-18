package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeColeccion;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.RechazosDeCambio;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.RechazosDeEliminacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

  public Long cantidadDeHechosReportadosEnUnaColeccion(Coleccion coleccion, LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select sum(e.total_hechos) " +
                "from ResultadoEstudioColeccion e " +
                "where e.fecha between :desde and :hasta " +
                "and e.coleccion = :coleccion ",
            Long.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setParameter("coleccion", coleccion)
        .getResultStream()
        .findFirst()
        .orElse(0L);
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

  public Long cantidadDeHechosReportadosEnUnaCategoria(String categoria, LocalDateTime desde, LocalDateTime hasta) {
    return entityManager().createQuery(
            "select sum(e.total_hechos) " +
                "from ResultadoEstudioCategoria e " +
                "where e.fecha between :desde and :hasta " +
                "and e.categoria = :categoria ",
            Long.class
        )
        .setParameter("desde", desde)
        .setParameter("hasta", hasta)
        .setParameter("categoria", categoria)
        .getResultStream()
        .findFirst()
        .orElse(0L);
  }

  // Spam
  public List<RechazosDeEliminacion> resultadosEstudioSpam() {
    return entityManager().createQuery(
        "select e " +
            "from RechazosDeEliminacion e ",
        RechazosDeEliminacion.class
    ).getResultList();
  }

  public String hechoMasSpameado() {
    return entityManager().createQuery(
            "select e.tituloHecho " +
                "from RechazosDeEliminacion e " +
                "order by e.cantidadSpam desc", String.class
        )
        .setMaxResults(1)
        .getSingleResult();
  }

  public Long cantidadRechazosTotal() {
    return entityManager().createQuery(
        "select sum(e.cantidadRechazadas) " +
            "from RechazosDeEliminacion e",
        Long.class
    ).getSingleResult();
  }

  public Long cantidadDeRechazosSpam() {
    return entityManager().createQuery(
        "select sum(e.cantidadSpam) " +
            "from RechazosDeEliminacion e",
        Long.class
    ).getSingleResult();
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
        .getResultStream()
        .findFirst()
        .orElse(null);
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
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

  public Optional<LocalDate> ultimaConsulta() {
    return entityManager().createQuery(
            "select max(fecha) from ResultadoEstudioColeccion",
            LocalDateTime.class
        )
        .getResultList()
        .stream()
        .filter(Objects::nonNull)
        .findFirst()
        .map(LocalDateTime::toLocalDate);
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
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

  public long cantidadDeSpamEnSolicitudesDeEliminacion() {
    return entityManager().createQuery(
            "select sum(r.cantidadSpam)" +
                "from RechazosDeEliminacion r ",
        Long.class
        )
        .getSingleResult();
  }

  public void eliminarEstadisticasDeColeccion(Coleccion coleccion) {
    entityManager().createQuery(
        "delete from ResultadoEstudioColeccion ec" +
            " where ec.coleccion = :coleccion "
    ).setParameter("coleccion", coleccion)
        .executeUpdate();
  }
}
