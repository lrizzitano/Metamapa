package ar.edu.utn.frba.dds.Hechos;

import ar.edu.utn.frba.dds.Fuentes.Fuente;
import ar.edu.utn.frba.dds.Solicitudes.Solicitudes;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Predicate<Hecho> criterioDePertenencia;
  private final Fuente fuente;

  public Coleccion(String titulo, String descripcion,
                   Predicate<Hecho> criterioDePertenencia, Fuente fuente) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.criterioDePertenencia = requireNonNull(criterioDePertenencia);
    this.fuente = requireNonNull(fuente);
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Set<Hecho> hechos(Predicate<Hecho> filtro) {
    return fuente.obtenerHechos(filtro.and(criterioDePertenencia).and(condicionNoEliminado()));
  }

  private Predicate<Hecho> condicionNoEliminado() {
    return hecho -> !Solicitudes.instance().estaEliminado(hecho);
  }
}
