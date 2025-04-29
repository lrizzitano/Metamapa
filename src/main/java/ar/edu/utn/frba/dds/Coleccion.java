package ar.edu.utn.frba.dds;

import java.util.Set;
import static java.util.Objects.requireNonNull;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Filtro criterioDePertenencia;
  private final Fuente fuente;

  public Coleccion(String titulo, String descripcion, Filtro criterioDePertenencia, Fuente fuente) {
    this.titulo = requireNonNull(titulo);
    this.descripcion = requireNonNull(descripcion);
    this.criterioDePertenencia = requireNonNull(criterioDePertenencia);
    this.fuente = requireNonNull(fuente);
  }

  public String getTitulo() { return titulo; }

  public String getDescripcion() { return descripcion; }

  public Set<Hecho> hechos(Filtro filtro) {
    return fuente.obtenerHechos(
        (Hecho hecho) ->(
            !Solicitudes.instance().hechosEliminados().contains(hecho.getTitulo())
            && criterioDePertenencia.aplicar(hecho)
            && filtro.aplicar(hecho)
        )
    );
  }
}
