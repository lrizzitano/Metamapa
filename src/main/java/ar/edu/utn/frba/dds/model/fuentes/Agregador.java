package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("agregador")
public class Agregador extends Fuente implements Calendarizable  {

  public Agregador() {}

  @ManyToMany
  @JoinColumn(name = "fuentes_agregador")
  public Set<Fuente> fuentes;

  //@OneToMany
  //@JoinColumn(name = "hechos_agregador")
  @Transient
  private Set<Hecho> hechos = Set.of();

  @Column(name = "proxima_actualizacion_agregador")
  private LocalDateTime proximaActualizacion;

  @Column(name = "frecuencia_actualizacion_agregador")
  private Duration frecuencia;

  public Agregador(Set<Fuente> fuentes, LocalDateTime proximaActualizacion, Duration frecuencia) {
    this.fuentes = new HashSet<>(fuentes);
    this.proximaActualizacion = proximaActualizacion;
    this.frecuencia = frecuencia;
  }

  public void agregarFuente(Fuente fuente) {
    this.fuentes.add(fuente);
  }

  public void eliminarFuente(Fuente fuente) {
    this.fuentes.remove(fuente);
  }


  @Override
  public LocalDateTime proximaActualizacion() {
    return proximaActualizacion;
  }

  @Override
  public void actualizar() {
    Filtro filtroNull = new NullFiltro();

    this.hechos =  this.fuentes.stream()
        .flatMap(f -> f.obtenerHechos(filtroNull).stream())
        .collect(Collectors.toSet());

    this.proximaActualizacion = LocalDateTime.now().plus(this.frecuencia);
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {

  return this.fuentes.stream()
      .flatMap(f -> f.obtenerHechos(filtro).stream())
      .collect(Collectors.toSet());
  }

  public void setFrecuencia(Duration frecuencia) {
    this.frecuencia = frecuencia;
  }

  @Override
  public String getNombre(){
    return " Fuentes: #" + fuentes.stream().map(Fuente::getNombre).collect(Collectors.joining(" #"));
  }
}















