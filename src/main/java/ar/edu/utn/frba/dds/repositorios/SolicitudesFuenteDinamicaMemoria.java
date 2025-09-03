package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Implementacion de repositorio con almacenamiento en memoria
public class SolicitudesFuenteDinamicaMemoria implements SolicitudDeCambioRepository, WithSimplePersistenceUnit {
  private final Set<SolicitudDeCambio> pendientes = new HashSet<>();
  private final Set<SolicitudDeCambio> aceptadas = new HashSet<>();
  private final Map<Hecho, Integer> rechazadas = new HashMap<>();

  public SolicitudesFuenteDinamicaMemoria() {
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
  public void nuevaSolicitud(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.add(solicitudDeCambio);
  }

  @Override
  public void aceptarSolicitud(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.remove(solicitudDeCambio);
    this.aceptadas.add(solicitudDeCambio);
  }

  @Override
  public void rechazarSolicitud(SolicitudDeCambio solicitudDeCambio) {
    this.pendientes.remove(solicitudDeCambio);
    Hecho hecho = solicitudDeCambio.getHechoParacambiar();
    rechazadas.put(hecho, rechazadas.getOrDefault(hecho, 0) + 1);
  }
}
