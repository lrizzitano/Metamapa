package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import java.util.HashSet;
import java.util.Set;

public class FuentesRepositoryMemoria implements FuentesRepository {

  private FuentesRepositoryMemoria() {

  }

  private static final FuentesRepository instance = new FuentesRepositoryMemoria();

  public static FuentesRepository instance() {
    return instance;
  }

  public Set<Fuente> fuentes = new HashSet<>();

  public void agregarFuente(Fuente fuente) {
    fuentes.add(fuente);
  }

  public void eliminarFuente(Fuente fuente) {
    fuentes.remove(fuente);
  }

  public Set<Fuente> obtenerFuentes() {
    return this.fuentes;
  }

  public void reset() {
    fuentes.clear();
  }
}
