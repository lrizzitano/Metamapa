package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.filtros.FiltroEliminados;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.solicitudes.Solicitudes;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Set;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final FiltroCompuesto criterioDePertenencia;
  private final Fuente fuente;

  public Coleccion(String titulo, String descripcion,
                   Filtro criterioDePertenencia, Fuente fuente) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    requireNonNull(criterioDePertenencia);
    this.criterioDePertenencia = new FiltroCompuesto(Collections.singletonList(criterioDePertenencia));
    this.fuente = requireNonNull(fuente);
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Set<Hecho> hechos(Filtro filtro) {
    return fuente.obtenerHechos(criterioDePertenencia.and(filtro).and(this.condicionNoEliminado()));
  }

  private Filtro condicionNoEliminado() {
    return new FiltroEliminados(Solicitudes.instance());
  }
}
