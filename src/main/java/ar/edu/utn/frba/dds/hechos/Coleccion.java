package ar.edu.utn.frba.dds.hechos;

import static java.util.Objects.requireNonNull;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacionRepository;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
  public Long idd;

  @Column(name = "titulo",nullable = false)
  private String titulo;

  @Column(name = "descripcion",nullable = false)
  private String descripcion;

  @Transient
  //@ManyToOne
  //@JoinColumn(name = "criterioDePertenencia",nullable = false)
  private Filtro criterioDePertenencia;

  @Transient
  //@ManyToOne
  //@JoinColumn(name = "fuente",nullable = false)
  private Fuente fuente;

  @Transient
  private final String id = UUID.randomUUID().toString();

  @ManyToOne
  @JoinColumn(name = "criterioConsenso",nullable = false)
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

  public String getId() {
    return id;
  }

  public void setCriterioConsenso(Consenso criterioConsenso) {
    this.criterioConsenso = criterioConsenso;
  }

  public Set<Hecho> hechos(Filtro filtro) {
    return this.streamFiltradaBase(filtro).collect(Collectors.toSet());
  }

  public Set<Hecho> hechosConsensuados(Filtro filtro) {
    return this.streamFiltradaBase(filtro)
        .filter(criterioConsenso::esConsensuado)
        .collect(Collectors.toSet());
  }

  private Stream<Hecho> streamFiltradaBase(Filtro filtro) {
    Set<Hecho> eliminados = solicitudes.hechosEliminados();
    FiltroCompuesto filtroCompuesto =
        new FiltroCompuesto(Collections.singletonList(criterioDePertenencia));
    return fuente.obtenerHechos(filtroCompuesto.and(filtro)).stream()
        .filter(h -> !eliminados.contains(h));
  }
}
