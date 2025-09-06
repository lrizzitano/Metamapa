package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SolicitudesFuenteDinamicaJPA extends RepoGenerico<SolicitudDeCambio> implements SolicitudDeCambioRepository, WithSimplePersistenceUnit {

  public SolicitudesFuenteDinamicaJPA() {
    super(SolicitudDeCambio.class);
  }

  @Override
  public void nuevaSolicitud(SolicitudDeCambio solicitudDeCambio) {
    super.save(solicitudDeCambio);
  }

  @Override
  public void aceptarSolicitud(SolicitudDeCambio solicitudDeCambio) {
    entityManager().createQuery(
        "UPDATE SolicitudDeCambio sc " +
            "SET sc.fueAceptada = :fueAceptada " +
            "WHERE sc.id = :idSolicitudDeCambio")
        .setParameter("idSolicitudDeCambio", solicitudDeCambio.getId())
        .setParameter("fueAceptada", true)
        .executeUpdate();
  }

  @Override
  public void rechazarSolicitud(SolicitudDeCambio solicitudDeCambio) {
    entityManager().remove(solicitudDeCambio);
    // TODO: Agregar al conteo de solicitudes rechazadas para el hecho
  }

  @Override
  public Set<SolicitudDeCambio> getPendientes() {
    return this.getByEstado(false);
  }

  @Override
  public Set<SolicitudDeCambio> getAceptadas() {
    return this.getByEstado(true);
  }

  // TODO: manejar la persistencia de las solicitudes rechazadas como un mapa de hecho a int
  @Override
  public Map<Hecho, Integer> getRechazadas() {
    throw new UnsupportedOperationException("Falta implementar, disculpe las moletias");
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
