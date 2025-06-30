package ar.edu.utn.frba.dds.hechos;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.filtros.FiltroEliminados;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.consenso.CriterioConsenso;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacionRepository;
import ar.edu.utn.frba.dds.solicitudes.SolicitudesDeEliminacion;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final FiltroCompuesto criterioDePertenencia;
  private final Fuente fuente;
  private final String id = UUID.randomUUID().toString();
  private final CriterioConsenso criterioConsenso;
  private final SolicitudDeEliminacionRepository solicitudes;

  public Coleccion(String titulo, String descripcion,
                   Filtro criterioDePertenencia,
                   Fuente fuente, CriterioConsenso criterioConsenso,
                   SolicitudDeEliminacionRepository solicitudes) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.criterioConsenso = requireNonNull(criterioConsenso);
    requireNonNull(criterioDePertenencia);
    this.criterioDePertenencia =
        new FiltroCompuesto(Collections.singletonList(criterioDePertenencia));
    this.fuente = requireNonNull(fuente);
    this.solicitudes = solicitudes;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getId() {
    return id;
  }

  public Set<Hecho> hechos(Filtro filtro) {
    return this.streamFiltradaBase(filtro)
        .collect(Collectors.toSet());
  }

  public Set<Hecho> hechosConsensuados(Filtro filtro) {
    return this.streamFiltradaBase(filtro)
        .filter(this.condicionConsenso())
        .collect(Collectors.toSet());
  }

  private Stream<Hecho> streamFiltradaBase(Filtro filtro) {
    return fuente.obtenerHechos(criterioDePertenencia.and(filtro)).stream()
        .filter(this.condicionNoEliminado());
  }

  private Predicate<Hecho> condicionNoEliminado() {
    return h -> {
      Set<Hecho> eliminados = solicitudes.hechosEliminados();
      return !eliminados.contains(h);
    };
  }

  private Predicate<Hecho> condicionConsenso() {
    return hecho-> criterioConsenso.getHechosConsensuados().contains(hecho);
  }
}
