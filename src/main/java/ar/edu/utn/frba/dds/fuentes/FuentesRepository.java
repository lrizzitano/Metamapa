package ar.edu.utn.frba.dds.fuentes;

import java.util.HashSet;
import java.util.Set;

public class FuentesRepository {

  private FuentesRepository() {}
  private static final FuentesRepository instance = new FuentesRepository();

  public static FuentesRepository instance() {return instance;}

  public Set<Fuente> fuentes = new HashSet<>();

  public void agregarFuente(Fuente fuente) {
    fuentes.add(fuente);
  }

  public void eliminarFuente(Fuente fuente) {
    fuentes.remove(fuente);
  }

  public Set<Fuente> getFuentes(){
    return this.fuentes;
  }
}
