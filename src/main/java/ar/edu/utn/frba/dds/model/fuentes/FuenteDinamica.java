package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudDeCambioRepository;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("dinamica")
public class FuenteDinamica extends Fuente {

  public FuenteDinamica() {}

  @Transient
  private static final FuenteDinamica instance = new FuenteDinamica();

  @Transient
  private HechoRepository repositorioDeHechos;

  @Transient
  private SolicitudDeCambioRepository repositorioDeSolicitudes;

  // inyecto por setter porque es singleton (no puedo inyectar en constructor)
  // -> podria dejar de serlo e inyecto por constructor
  public void setHechoRepository(HechoRepository repository) {
    this.repositorioDeHechos = repository;
  }

  public void setSolicitudesRepository(SolicitudDeCambioRepository repository) {
    this.repositorioDeSolicitudes = repository;
  }

  public static FuenteDinamica instance() {
    return instance;
  }

  public void nuevaSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.repositorioDeSolicitudes.nuevaSolicitud(solicitudDeCambio);
  }

  public void aceptarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.repositorioDeHechos.actualizar(solicitudDeCambio.getHechoParacambiar(),
        solicitudDeCambio.getHechoModificado());
    this.repositorioDeSolicitudes.aceptarSolicitud(solicitudDeCambio);
  }

  public void rechazarSolicitudDeCambio(SolicitudDeCambio solicitudDeCambio) {
    this.repositorioDeSolicitudes.rechazarSolicitud(solicitudDeCambio);
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return this.repositorioDeHechos.obtenerHechos(filtro);
  }

  @Override
  public Set<Hecho> obtenerHechos(String busqueda, Filtro filtro) {
    Set<Hecho> hechosBusqueda = this.repositorioDeHechos.fullTextSearch(busqueda);

    return hechosBusqueda.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());

  }

  public void agregarHecho(Hecho hecho) {
    hecho.contribuyente().contribuir();
    this.repositorioDeHechos.agregar(hecho);
  }

  public void eliminarHecho(Hecho hecho) {
    this.repositorioDeHechos.eliminar(hecho);
  }
}