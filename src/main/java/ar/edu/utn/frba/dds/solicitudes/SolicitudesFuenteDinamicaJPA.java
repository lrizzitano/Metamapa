package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SolicitudesFuenteDinamicaJPA implements SolicitudDeCambioRepository, WithSimplePersistenceUnit {

  public SolicitudesFuenteDinamicaJPA() {}

  public Set<SolicitudDeCambio> getPendientes() {
    return this.getByEstado(false);
  }

  public Set<SolicitudDeCambio> getAceptadas() {
    return this.getByEstado(true);
  }

  // TODO: manejar la persistencia de las solicitudes rechazadas como un mapa de hecho a int
  public Map<Hecho, Integer> getRechazadas() {
    return new HashMap<Hecho, Integer>();
  }

  @Override
  public void crear(SolicitudDeCambio solicitudDeCambio) {
    entityManager().persist(solicitudDeCambio);
  }

  @Override
  public void aceptar(SolicitudDeCambio solicitudDeCambio) {
    entityManager().createQuery(
        "UPDATE SolicitudDeCambio sc " +
            "SET sc.fueAceptada = :fueAceptada " +
            "WHERE sc.id = :idSolicitudDeCambio")
        .setParameter("idSolicitudDeCambio", solicitudDeCambio.getId())
        .setParameter("fueAceptada", true)
        .executeUpdate();
  }

  @Override
  public void rechazar(SolicitudDeCambio solicitudDeCambio) {
    entityManager().remove(solicitudDeCambio);
    // TODO: Agregar al conteo de solicitudes rechazadas para el hecho
  }

  private Set<SolicitudDeCambio> getByEstado(Boolean estado) {
    return new HashSet<>(
        entityManager().createQuery(
                "FROM SolicitudDeCambio sc " +
                    "WHERE sc.fueAceptada = :aceptada ", SolicitudDeCambio.class)
            .setParameter("aceptada", estado)
            .getResultList()
    );
  }
}
