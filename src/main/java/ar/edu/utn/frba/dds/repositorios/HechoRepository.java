package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.Set;

public interface HechoRepository {
  void actualizar(Hecho hechoParaCambiar, Hecho hechoModificado);

  Set<Hecho> obtenerTodos();

  Set<Hecho> obtenerHechos(Filtro filtro);

  void agregar(Hecho hecho);

  void eliminar(Hecho hecho);

  void marcarComoRevisado(Hecho hecho);

  Set<Hecho> obtenerNoRevisados();
}