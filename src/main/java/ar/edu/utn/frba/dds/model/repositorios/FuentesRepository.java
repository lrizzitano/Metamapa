package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import java.util.Set;

public interface FuentesRepository {

  void agregarFuente(Fuente fuente);

  void eliminarFuente(Fuente fuente);

  Set<Fuente> obtenerFuentes();
}
