package ar.edu.utn.frba.dds.repositorios;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.NullDetector;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SolicitudesDeEliminacionJPA implements SolicitudDeEliminacionRepository, WithSimplePersistenceUnit {

  private DetectorDeSpam detectorDeSpam;

  public SolicitudesDeEliminacionJPA() {
    this.detectorDeSpam = new NullDetector();
  }

  public void setDetectorDeSpam(DetectorDeSpam detectorDeSpam) {
    this.detectorDeSpam = requireNonNull(detectorDeSpam);
  }

  @Override
  public void nuevaSolicitud(SolicitudDeEliminacion solicitud) {
    if (detectorDeSpam.esSpam(solicitud.getFundamento())) {
      this.rechazarSolicitud(solicitud);
      return;
    }

    entityManager().persist(solicitud);
  }

  @Override
  public void aceptarSolicitud(SolicitudDeEliminacion solicitudDeEliminacion) {
    entityManager().createQuery(
            "UPDATE SolicitudDeEliminacion se " +
                "SET se.fueAceptada = :fueAceptada " +
                "WHERE se.id = :idSolicitudDeEliminacion")
        .setParameter("idSolicitudDeEliminacion", solicitudDeEliminacion.getId())
        .setParameter("fueAceptada", true)
        .executeUpdate();
  }

  @Override
  public void rechazarSolicitud(SolicitudDeEliminacion solicitudDeCambio) {
    entityManager().remove(solicitudDeCambio);
    // TODO: Agregar al conteo de solicitudes rechazadas para el hecho
  }

  @Override
  public Set<SolicitudDeEliminacion> getPendientes() {
    return this.getByEstado(false);
  }

  @Override
  public Set<SolicitudDeEliminacion> getAceptadas() {
    return this.getByEstado(true);
  }

  // TODO: manejar la persistencia de las solicitudes rechazadas como un mapa de hecho a int
  @Override
  public Map<Hecho, Integer> getRechazadas() {
    throw new UnsupportedOperationException("Falta implementar, disculpe las moletias");
  }

  @Override
  public Integer getRechazos(Hecho hecho) {
    throw new UnsupportedOperationException("Falta implementar, disculpe las moletias");
  }

  @Override
  public boolean estaEliminado(Hecho hecho) {
    Long count = entityManager().createQuery(
            "SELECT COUNT(se) " +
                "FROM SolicitudDeEliminacion se " +
                "WHERE se.fueAceptada = :fueAceptada " +
                "AND se.hecho = :hecho", Long.class)
        .setParameter("fueAceptada", true)
        .setParameter("hecho", hecho)
        .getSingleResult();

    return count > 0;
  }

  @Override
  public Set<Hecho> hechosEliminados() {
    Set<SolicitudDeEliminacion> aceptadas = this.getAceptadas();
    return new HashSet<>(aceptadas.stream().map(SolicitudDeEliminacion::getHecho).toList());
  }

  private Set<SolicitudDeEliminacion> getByEstado(Boolean estado) {
    return new HashSet<>(
        entityManager().createQuery(
                "FROM SolicitudDeEliminacion se " +
                    "WHERE se.fueAceptada = :aceptada ", SolicitudDeEliminacion.class)
            .setParameter("aceptada", estado)
            .getResultList()
    );
  }
}
