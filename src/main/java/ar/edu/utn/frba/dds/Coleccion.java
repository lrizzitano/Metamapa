package ar.edu.utn.frba.dds;

import java.util.Set;
import static java.util.Objects.requireNonNull;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private Filtro criterioDePertenencia;
  private Fuente fuente;

  public Coleccion(String titulo, String descripcion, Filtro criterioDePertenencia, Fuente fuente) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.criterioDePertenencia = requireNonNull(criterioDePertenencia);
    this.fuente = requireNonNull(fuente);
  }

  public Set<Hecho> hechos(Filtro filtro) {
    return fuente.obtenerHechos(
        (Hecho hecho) ->(
            !Solicitudes.instance().hechosEliminados().contains(hecho)
            && criterioDePertenencia.aplicar(hecho)
            && filtro.aplicar(hecho)
        )
    );
  }
}
