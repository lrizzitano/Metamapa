package ar.edu.utn.frba.dds.execpciones;

public class NoSePuedeEliminarUnHechoQueNoExiste extends RuntimeException {
  public NoSePuedeEliminarUnHechoQueNoExiste(String message) {
    super("No puedes eliminar un hecho que no existe en la fuente dinamica" + message);
  }
}
