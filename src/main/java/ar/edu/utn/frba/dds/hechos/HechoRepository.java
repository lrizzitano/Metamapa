package ar.edu.utn.frba.dds.hechos;

import java.util.Set;

public interface HechoRepository {
  void actualizar(Hecho hechoACambiar, Hecho hechoModificado);
  Set<Hecho> obtenerTodos();
  void agregar(Hecho hecho);
  void eliminar(Hecho hecho);
}