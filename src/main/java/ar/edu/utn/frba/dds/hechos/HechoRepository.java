package ar.edu.utn.frba.dds.hechos;

import java.util.List;
import java.util.Set;

public interface HechoRepository {
  void actualizar(Hecho hechoParaCambiar, Hecho hechoModificado);

  Set<Hecho> obtenerTodos();

  void agregar(Hecho hecho);

  void eliminar(Hecho hecho);

  void marcarComoRevisado(Hecho hecho);

  Set<Hecho> obtenerNoRevisados();
}