package ar.edu.utn.frba.dds.model.repositorios.solicitudes;

import ar.edu.utn.frba.dds.model.repositorios.RepoGenerico;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam.NullDetector;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class SolicitudesDeEliminacionJPA extends RepoGenerico<SolicitudDeEliminacion> implements SolicitudDeEliminacionRepository, WithSimplePersistenceUnit {

  private DetectorDeSpam detectorDeSpam;

  public SolicitudesDeEliminacionJPA() {
    super(SolicitudDeEliminacion.class);
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

    super.save(solicitud);
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
  public void rechazarSolicitud(SolicitudDeEliminacion solicitud) {
    String hecho = solicitud.getTituloHecho();

    RechazosDeEliminacion rechazos = entityManager().createQuery(
            "FROM RechazosDeEliminacion r WHERE r.tituloHecho = :hecho", RechazosDeEliminacion.class)
        .setParameter("hecho", hecho)
        .getResultStream()
        .findFirst()
        .orElse(null);

    if (rechazos == null) {

      rechazos = new RechazosDeEliminacion(
          hecho,
          1,
          detectorDeSpam.esSpam(solicitud.getFundamento()) ? 1 : 0
      );
      entityManager().persist(rechazos);

    } else {
      rechazos.sumarRechazo();

      if (detectorDeSpam.esSpam(solicitud.getFundamento())) {
        rechazos.sumarSpam();
      }

      entityManager().merge(rechazos);
    }

    entityManager().remove(solicitud);
  }

  @Override
  public Set<SolicitudDeEliminacion> getPendientes() {
    return this.getByEstado(false);
  }

  @Override
  public Set<SolicitudDeEliminacion> getAceptadas() {
    return this.getByEstado(true);
  }

  @Override
  public Set<RechazosDeEliminacion> getRechazadas() {
    return entityManager().createQuery(
        "FROM RechazosDeEliminacion r", RechazosDeEliminacion.class)
        .getResultStream()
        .collect(Collectors.toSet()
        );
  }

  @Override
  public Integer getRechazos(String tituloHecho) {
    return entityManager().createQuery(
            "SELECT r.cantidadRechazadas FROM RechazosDeEliminacion r WHERE r.tituloHecho = :hecho", Integer.class)
        .setParameter("hecho", tituloHecho)
        .getResultStream()
        .findFirst()
        .orElse(0);
  }


  @Override
  public boolean estaEliminado(String tituloHecho) {
    Long count = entityManager().createQuery(
            "SELECT COUNT(se) " +
                "FROM SolicitudDeEliminacion se " +
                "WHERE se.fueAceptada = :fueAceptada " +
                "AND se.tituloHecho = :hecho", Long.class)
        .setParameter("fueAceptada", true)
        .setParameter("hecho", tituloHecho)
        .getSingleResult();

    return count > 0;
  }

  @Override
  public Set<String> hechosEliminados() {
    Set<SolicitudDeEliminacion> aceptadas = this.getAceptadas();
    return aceptadas.stream().map(SolicitudDeEliminacion::getTituloHecho).collect(Collectors.toSet());
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
