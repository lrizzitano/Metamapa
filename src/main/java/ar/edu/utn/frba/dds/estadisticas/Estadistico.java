package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.hechos.Coleccion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Estadistico implements WithSimplePersistenceUnit {

  public Estadistico() {}

  public String provinciaConMayorCantidadDeHechosReportadosDeColeccion(Coleccion coleccion, LocalDateTime fecha) {
    return (String)  entityManager().createNativeQuery(
            "select r.provincia " +
                "from EstudioColeccion e " +
                "join ResultadoEstudioColeccion_hechosPorProvincia r " +
                "  ON r.resultadoestudiocoleccion_estudio_id = e.estudio_id " +
                "where e.fecha = :fechaP and e.coleccion_id = :coleccionID " +
                "group by r.provincia " +
                "order by SUM(r.hechos_de_provincia) desc limit 1"
        )
        .setParameter("fechaP", fecha)
        .setParameter("coleccionID", coleccion.getId())
        .getSingleResult();
  }

  public String categoriaConMasHechosReportados(LocalDateTime fecha) {
    return (String) entityManager().createNativeQuery(
            "select categoria from EstudioCategoria " +
                "where fecha = :fecha " +
                "group by categoria " +
                "order by SUM(total_hechos) desc"
        )
        .setParameter("fecha", fecha)
        .setMaxResults(1)
        .getSingleResult();
  }

  public String provinciaConMasHechosReportadosDeUnaCategoria(String categoria, LocalDateTime fecha) {
    return (String) entityManager().createNativeQuery(
            "select r.provincia " +
                "from EstudioCategoria e " +
                "join ResultadoEstudioCategoria_hechosPorProvincia r " +
                "  ON r.resultadoestudiocategoria_estudio_id = e.estudio_id " +
                "where e.fecha = :fecha and e.categoria = :categoria " +
                "group by r.provincia,e.categoria " +
                "order by SUM(r.hechos_de_provincia) desc limit 1"
        )
        .setParameter("fecha", fecha)
        .setParameter("categoria", categoria)
        .getSingleResult();
  }

  public LocalTime horaPicoDeReporteDeUnaCategoria(String categoria, LocalDateTime fecha) {
    Object result = entityManager().createNativeQuery(
            "select hora_pico from EstudioCategoria " +
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
