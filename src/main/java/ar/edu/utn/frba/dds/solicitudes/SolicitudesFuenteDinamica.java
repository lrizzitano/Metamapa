package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Implementacion de repositorio con almacenamiento en memoria
public class SolicitudesFuenteDinamica implements SolicitudDeCambioRepository {
  private final Set<SolicitudDeCambio> pendientes = new HashSet<>();
  private final Set<SolicitudDeCambio> aceptadas = new HashSet<>();
  private final Map<Hecho, Integer> rechazadas = new HashMap<>();

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
    Hecho hecho = solicitudDeCambio.getHechoACambiar();
    rechazadas.put(hecho, rechazadas.getOrDefault(hecho, 0) + 1);
  }
}
