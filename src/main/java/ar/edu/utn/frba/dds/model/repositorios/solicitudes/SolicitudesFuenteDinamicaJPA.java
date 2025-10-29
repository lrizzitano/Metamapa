package ar.edu.utn.frba.dds.model.repositorios.solicitudes;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.repositorios.RepoGenerico;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeCambio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
  public void rechazarSolicitud(SolicitudDeCambio solicitud) {
    Hecho hecho = solicitud.getHechoParacambiar();

    RechazosDeCambio rechazos = entityManager().createQuery(
            "FROM RechazosDeCambio r WHERE r.hecho = :hecho", RechazosDeCambio.class)
        .setParameter("hecho", hecho)
        .getResultStream()
        .findFirst()
        .orElse(null);

    if (rechazos == null) {
      rechazos = new RechazosDeCambio(hecho, 1);
      entityManager().persist(rechazos);
    } else {
      rechazos.sumarRechazo();
      entityManager().merge(rechazos);
    }

    entityManager().remove(solicitud);
  }

  @Override
  public Set<SolicitudDeCambio> getPendientes() {
    return this.getByEstado(false);
  }

  @Override
  public Set<SolicitudDeCambio> getAceptadas() {
    return this.getByEstado(true);
  }

  @Override
  public Map<Hecho, Integer> getRechazadas() {
    return entityManager().createQuery(
            "FROM RechazosDeCambio r", RechazosDeCambio.class)
        .getResultStream()
        .collect(Collectors.toMap(
            RechazosDeCambio::getHecho,
            RechazosDeCambio::getCantidad
        ));
  }

  @Override
  public Integer getRechazos(Hecho hecho) {
    return entityManager().createQuery(
            "SELECT r.cantidad FROM RechazosDeCambio r WHERE r.hecho = :hecho", Integer.class)
        .setParameter("hecho", hecho)
        .getResultStream()
        .findFirst()
        .orElse(0);
  }

  public Set<SolicitudDeCambio> getByEstado(Boolean estado) {
    return new HashSet<>(
        entityManager().createQuery(
                "FROM SolicitudDeCambio sc " +
                    "WHERE sc.fueAceptada = :aceptada ", SolicitudDeCambio.class)
            .setParameter("aceptada", estado)
            .getResultList()
    );
  }
}
