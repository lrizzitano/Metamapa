package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class Agregador implements Calendarizable, Fuente {
  private final Set<Fuente> fuentes;
  private Set<Hecho> hechos = Set.of();
  private LocalDateTime proximaActualizacion;

  public Agregador(Set<Fuente> fuentes, LocalDateTime proximaActualizacion) {
    this.fuentes = new HashSet<>(fuentes);
    this.proximaActualizacion = proximaActualizacion;
  }

  public void agreggarFuente(Fuente fuente){
    this.fuentes.add(fuente);
  }

  public void elimnarFuente(Fuente fuente){
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

    this.proximaActualizacion = LocalDateTime.now().plusHours(4);
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro){
    return this.hechos.stream()
        .filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }
}















