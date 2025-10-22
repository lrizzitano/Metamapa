package ar.edu.utn.frba.dds.hechos;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudDeEliminacionRepository;
import org.hibernate.annotations.Cascade;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Entity
@Table(name = "Coleccion")
public class Coleccion{

  public Coleccion(){}

  @Id
  @Column(name = "coleccion_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(name = "titulo",nullable = false)
  private String titulo;

  @Column(name = "descripcion",nullable = false)
  private String descripcion;

  @ManyToOne
  @JoinColumn(name = "criterioDePertenencia",nullable = false)
  @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
  private Filtro criterioDePertenencia;

  @ManyToOne
  @JoinColumn(name = "fuente",nullable = false)
  private Fuente fuente;

  @ManyToOne
  @JoinColumn(name = "criterioConsenso",nullable = false)
  @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
  private Consenso criterioConsenso;

  @Transient
  private SolicitudDeEliminacionRepository solicitudes;

  public Coleccion(String titulo, String descripcion,
                   Filtro criterioDePertenencia,
                   Fuente fuente, Consenso criterioConsenso,
                   SolicitudDeEliminacionRepository solicitudes) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.criterioConsenso = requireNonNull(criterioConsenso);
    requireNonNull(criterioDePertenencia);
    this.criterioDePertenencia = criterioDePertenencia;
    this.fuente = requireNonNull(fuente);
    this.solicitudes = solicitudes;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Long getId() {
    return id;
  }

  public void setCriterioConsenso(Consenso criterioConsenso) {
    this.criterioConsenso = criterioConsenso;
  }

  public Set<Hecho> hechos(Filtro filtro) {
    return this.streamFiltradaBase(filtro).collect(Collectors.toSet());
  }

  public Set<Hecho> hechos(String busqueda, Filtro filtro) {
    return this.streamFiltradaBase(busqueda, filtro).collect(Collectors.toSet());
  }

  public Set<Hecho> hechosConsensuados(Filtro filtro) {
    return this.streamFiltradaBase(filtro)
        .filter(criterioConsenso::esConsensuado)
        .collect(Collectors.toSet());
  }

  private Stream<Hecho> streamFiltradaBase(Filtro filtro) {
    Set<String> eliminados = solicitudes.hechosEliminados();

    FiltroCompuesto filtroCompuesto =
        new FiltroCompuesto(Collections.singletonList(criterioDePertenencia));

    return fuente.obtenerHechos(filtroCompuesto.and(filtro)).stream()
        .filter(h -> !eliminados.contains(h.titulo()));
  }

  private Stream<Hecho> streamFiltradaBase(String busqueda, Filtro filtro) {
    Set<String> eliminados = solicitudes.hechosEliminados();

    FiltroCompuesto filtroCompuesto =
        new FiltroCompuesto(Collections.singletonList(criterioDePertenencia));

    return fuente.obtenerHechos(busqueda, filtroCompuesto.and(filtro)).stream()
        .filter(h -> !eliminados.contains(h.titulo()));
  }
}
