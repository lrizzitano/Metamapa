package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.hechos.Coleccion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Estadistico implements WithSimplePersistenceUnit {

  public Estadistico() {}

  public String provinciaConMayorCantidadDeHechosReportadosDeColeccion(Coleccion coleccion, LocalDateTime fecha) {
    return (String) entityManager().createNativeQuery(
            "select provincia from ResultadoEstudioColeccion "
                + "where fecha = :fecha and coleccion_id = :coleccion"
        )
        .setParameter("fecha", fecha)
        .setParameter("coleccion", coleccion.getId())
        .getSingleResult();
  }

  public String categoriaConMasHechosReportados(LocalDateTime fecha) {
    return (String) entityManager().createNativeQuery(
            "select categoria from ResultadoEstudioCategoria " +
                "where fecha = :fecha " +
                "group by categoria " +
                "order by total_hechos desc"
        )
        .setParameter("fecha", fecha)
        .setMaxResults(1)
        .getSingleResult();
  }

  public String provinciaConMasHechosReportadosDeUnaCategoria(String categoria, LocalDateTime fecha) {
    return (String) entityManager().createNativeQuery(
            "select provincia from ResultadoEstudioCategoria " +
                "where fecha = :fecha and categoria = :categoria "
        )
        .setParameter("fecha", fecha)
        .setParameter("categoria", categoria)
        .getSingleResult();
  }

  public LocalTime horaPicoDeReporteDeUnaCategoria(String categoria, LocalDateTime fecha) {
    Object result = entityManager().createNativeQuery(
            "select hora_pico from ResultadoEstudioCategoria " +
                "where fecha = :fecha and categoria = :categoria"
        )
        .setParameter("fecha", fecha)
        .setParameter("categoria", categoria)
        .getSingleResult();

    if (result instanceof java.sql.Time) {
      return ((java.sql.Time) result).toLocalTime();
    }
    throw new IllegalStateException("El campo hora_pico no es de tipo TIME");
  }

  // TODO revisar que quede integrado con lo de los chicos
  public int cantidadDeSpamEnSolicitudesDeEliminacion(LocalDateTime fecha) {
    return (int) entityManager().createNativeQuery(
            "select cant_spam from RechazosDeEliminacion " +
                "where fecha = :fecha"
        )
        .setParameter("fecha", fecha)
        .getSingleResult();
  }
}
