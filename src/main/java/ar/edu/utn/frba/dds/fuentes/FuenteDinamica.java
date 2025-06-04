package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.HechoRepository;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambioRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FuenteDinamica implements Fuente {

  private FuenteDinamica() {}

  private static final FuenteDinamica instance = new FuenteDinamica();
  private HechoRepository hechos;
  private SolicitudDeCambioRepository solicitudes;

  // inyecto por setter porque es singleton (no puedo inyectar en constructor)
  // -> podria dejar de serlo e inyecto por constructor
  void setHechoRepository(HechoRepository repository) {
    this.hechos = repository;
  }

  void setSolicitudesRepository(SolicitudDeCambioRepository repository) {
    this.solicitudes = repository;
  }

  public static FuenteDinamica instance() {
    return instance;
  }

  public void nuevaSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.solicitudes.crear(solicitudDeCambio);
  }

  public void aceptarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.hechos.actualizar(solicitudDeCambio.getHechoParacambiar(),
        solicitudDeCambio.getHechoModificado());
    this.solicitudes.aceptar(solicitudDeCambio);
  }

  public void rechazarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.solicitudes.rechazar(solicitudDeCambio);
  }

  public Set<Hecho> obtenerHechos(Filtro filtro) {
    Map<String, Hecho> hechosPorTitulo = new HashMap<>();

    this.hechos.obtenerTodos()
        .stream()
        .filter(filtro.getAsPredicate())
        .forEach(hecho -> hechosPorTitulo.put(hecho.titulo(), hecho));

    return new HashSet<>(hechosPorTitulo.values());
  }

  public void agregarHecho(Hecho hecho) {
    hecho.contribuyente().contribuir();
    this.hechos.agregar(hecho);
  }

  public void eliminarHecho(Hecho hecho) {
    this.hechos.eliminar(hecho);
  }
}