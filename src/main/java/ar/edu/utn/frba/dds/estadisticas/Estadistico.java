package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Provincia;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Estadistico implements WithSimplePersistenceUnit {

  public Estadistico() {}

  public Provincia provinciaConMayorCantidadDeHechosReportadosDeColeccion(Coleccion coleccion, LocalDateTime fecha) {
    return entityManager().createQuery(
            "select r.provincia " +
                "from ResultadoEstudioColeccion e " +
                "join e.hechosPorProvincia r " +
                "where e.fecha = :fechaP and e.coleccion = :coleccion " +
                "group by r.provincia " +
                "order by sum(r.cant_hechos) desc",
            Provincia.class
        )
        .setParameter("fechaP", fecha)
        .setParameter("coleccion", coleccion)
        .setMaxResults(1)
        .getSingleResult();
  }

  public String categoriaConMasHechosReportados(LocalDateTime fecha) {
    return entityManager().createQuery(
            "select e.categoria " +
                "from ResultadoEstudioCategoria e " +
                "where e.fecha = :fecha " +
                "group by e.categoria " +
                "order by sum(e.total_hechos) desc",
            String.class
        )
        .setParameter("fecha", fecha)
        .setMaxResults(1)
        .getSingleResult();
  }

  public Provincia provinciaConMasHechosReportadosDeUnaCategoria(String categoria, LocalDateTime fecha) {
    return entityManager().createQuery(
            "select h.provincia " +
                "from ResultadoEstudioCategoria e " +
                "join e.hechosPorProvincia h " +
                "where e.fecha = :fecha and e.categoria = :categoria " +
                "group by h.provincia " +
                "order by sum(h.cant_hechos) desc",
            Provincia.class
        )
        .setParameter("fecha", fecha)
        .setParameter("categoria", categoria)
        .setMaxResults(1)
        .getSingleResult();
  }

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

  // TODO revisar que quede integrado con lo de los chicos
  public int cantidadDeSpamEnSolicitudesDeEliminacion(LocalDateTime fecha) {
    return (int) entityManager().createNativeQuery(
            "select cant_spam from RechazosDeEliminacion " +
                "where fecha = :fecha"
        )
        .getSingleResult();
  }
}
