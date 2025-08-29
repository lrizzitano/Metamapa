package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Implementacion de repositorio con almacenamiento en memoria
public class SolicitudesFuenteDinamica implements SolicitudDeCambioRepository, WithSimplePersistenceUnit {
  private final Set<SolicitudDeCambio> pendientes = new HashSet<>();
  private final Set<SolicitudDeCambio> aceptadas = new HashSet<>();
  private final Map<Hecho, Integer> rechazadas = new HashMap<>();

  public SolicitudesFuenteDinamica() {
  }

  public Set<SolicitudDeCambio> getPendientes() {
    return new HashSet<>(pendientes);
  }

  public Set<SolicitudDeCambio> getAceptadas() {
    return new HashSet<>(aceptadas);
  }

  public Map<Hecho, Integer> getRechazadas() {
    return new HashMap<>(rechazadas);
  }

  @Override
  public void crear(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.add(solicitudDeCambio);
  }

  @Override
  public void aceptar(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.remove(solicitudDeCambio);
    this.aceptadas.add(solicitudDeCambio);
  }

  @Override
  public void rechazar(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.remove(solicitudDeCambio);
    Hecho hecho = solicitudDeCambio.getHechoParacambiar();
    rechazadas.put(hecho, rechazadas.getOrDefault(hecho, 0) + 1);
  }

  public void persistir(SolicitudDeCambio solicitudDeCambio) {
    entityManager().persist(solicitudDeCambio);
  }

  public SolicitudDeCambio obetenerSolicitudDeCambio(Long id) {
    return entityManager().find(SolicitudDeCambio.class, id);
  }
}
