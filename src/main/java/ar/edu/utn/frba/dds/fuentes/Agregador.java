package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class Agregador implements Calendarizable, Fuente {
  public Set<Fuente> fuentes;
  public Set<Hecho> hechos = new HashSet<>();
  public Duration intervaloActualizacion = Duration.ofMinutes(24);
  public LocalDate ultimaActualizacion;

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
  public Boolean tocaActualizar() {
    //MARGEN DE 5 MINS ANTES O DESPUES.
    Duration transcurrido = Duration.between(ultimaActualizacion, LocalDate.now());

    Duration margenMin = intervaloActualizacion.plus(Duration.ofMinutes(5));
    Duration margenMax = intervaloActualizacion.minus(Duration.ofMinutes(5));

    return (!transcurrido.minus(margenMin).isNegative()) &&
          (transcurrido.compareTo(margenMax) <= 0);
  }

  @Override
  public void actualizar() {
    Filtro filtroNull = new NullFiltro();

    this.hechos =  this.fuentes.stream()
        .flatMap(f -> f.obtenerHechos(filtroNull).stream())
        .collect(Collectors.toSet());

    this.ultimaActualizacion = LocalDate.now();
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro){
    return this.hechos.stream()
        .filter(filtro.getAsPredicate()).collect(Collectors.toSet());
  }


}















