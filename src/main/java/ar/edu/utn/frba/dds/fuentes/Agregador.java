package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class Agregador implements Calendarizable, Fuente {
  public Set<Fuente> fuentes;
  public Set<Hecho> hechos = new HashSet<>();
  public Duration frecuencia = Duration.ofDays(24);
  public LocalDateTime ultimaActualizacion;

  public Agregador(Set<Fuente> fuentes) {
    this.fuentes = fuentes;
    this.actualizar();
  }

  public void agreggarFuente(Fuente fuente){
    this.fuentes.add(fuente);
  }

  public void elimnarFuente(Fuente fuente){
    this.fuentes.remove(fuente);
  }

  @Override
  public LocalDateTime ultimaActualizaion() {
    return this.ultimaActualizacion;
  }

  @Override
  public Duration frecuencia() {
    return this.frecuencia;
  }

  @Override
  public void actualizar() {
    Filtro filtroNull = new NullFiltro();

    this.hechos =  this.fuentes.stream()
        .flatMap(f -> f.obtenerHechos(filtroNull).stream())
        .collect(Collectors.toSet());

    this.ultimaActualizacion = LocalDateTime.now();
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro){
    return this.hechos.stream()
        .filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }
}















