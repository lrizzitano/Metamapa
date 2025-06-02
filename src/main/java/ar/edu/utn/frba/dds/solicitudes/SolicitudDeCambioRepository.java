package ar.edu.utn.frba.dds.solicitudes;

public interface SolicitudDeCambioRepository {

  void crear(SolicitudDeCambio solicitudDeCambio);

  void aceptar(SolicitudDeCambio solicitudDeCambio);

  void rechazar(SolicitudDeCambio solicitudDeCambio);
}
