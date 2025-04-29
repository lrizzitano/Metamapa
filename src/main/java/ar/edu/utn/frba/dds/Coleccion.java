package ar.edu.utn.frba.dds;

public class Coleccion {

  private String titulo;
  private String descripcion;
  private Filtro criterioDePertenencia;
  private Fuente fuente;

  public Set<Hecho> hechos(Filtro filtro) {
    return fuente.obtenerHechos(
        (Hecho hecho) ->(
            !Solicitudes.hechosEliminados().contains(hecho)
            && criterioDePertenencia.aplicar(hecho)
            && filtro.aplicar(hecho)
        )
    );
  }
}
