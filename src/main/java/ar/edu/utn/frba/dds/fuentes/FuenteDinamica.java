package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.HechoRepository;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambioRepository;
import java.util.Set;
import java.util.stream.Collectors;

public class FuenteDinamica implements Fuente {

  private FuenteDinamica() {}

  private static final FuenteDinamica instance = new FuenteDinamica();
  private HechoRepository repositorioDeHechos;
  private SolicitudDeCambioRepository repositorioDeSolicitudes;

  // inyecto por setter porque es singleton (no puedo inyectar en constructor)
  // -> podria dejar de serlo e inyecto por constructor
  void setHechoRepository(HechoRepository repository) {
    this.repositorioDeHechos = repository;
  }

  void setSolicitudesRepository(SolicitudDeCambioRepository repository) {
    this.repositorioDeSolicitudes = repository;
  }

  public static FuenteDinamica instance() {
    return instance;
  }

  public void nuevaSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.repositorioDeSolicitudes.crear(solicitudDeCambio);
  }

  public void aceptarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.repositorioDeHechos.actualizar(solicitudDeCambio.getHechoParacambiar(),
        solicitudDeCambio.getHechoModificado());
    this.repositorioDeSolicitudes.aceptar(solicitudDeCambio);
  }

  public void rechazarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.repositorioDeSolicitudes.rechazar(solicitudDeCambio);
  }

  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return this.repositorioDeHechos.obtenerTodos().stream()
        .filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }

  public void agregarHecho(Hecho hecho) {
    hecho.contribuyente().contribuir();
    this.repositorioDeHechos.agregar(hecho);
  }

  public void eliminarHecho(Hecho hecho) {
    this.repositorioDeHechos.eliminar(hecho);
  }
}