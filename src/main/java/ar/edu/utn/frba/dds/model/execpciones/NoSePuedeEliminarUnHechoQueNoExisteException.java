package ar.edu.utn.frba.dds.model.execpciones;

public class NoSePuedeEliminarUnHechoQueNoExisteException extends RuntimeException {
  public NoSePuedeEliminarUnHechoQueNoExisteException() {
    super("No puedes eliminar un hecho que no existe en la fuente dinamica");
  }
}
