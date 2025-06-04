package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import java.util.HashSet;
import java.util.Set;

public class HechosFuenteDinamica implements HechoRepository {
  private final Set<Hecho> hechos = new HashSet<>();

  public HechosFuenteDinamica() {}

  @Override
  public void actualizar(Hecho hechoACambiar, Hecho hechoModificado) {
    this.eliminar(hechoACambiar);
    this.agregar(hechoModificado);
  }

  @Override
  public Set<Hecho> obtenerTodos() {
    return this.hechos;
  }

  @Override
  public void agregar(Hecho hecho) {
    this.hechos.add(hecho);
  }

  @Override
  public void eliminar(Hecho hecho) {
    if(!hechos.contains(hecho)) {
      throw new NoSePuedeEliminarUnHechoQueNoExisteException();
    }
    this.hechos.remove(hecho);
  }
}
