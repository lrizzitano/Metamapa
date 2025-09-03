package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.HashSet;
import java.util.Set;

public class HechosFuenteDinamicaMemoria implements HechoRepository {
  private final Set<Hecho> hechosSinRevisar = new HashSet<>();
  private final Set<Hecho> hechosRevisados = new HashSet<>();

  public HechosFuenteDinamicaMemoria() {

  }

  @Override
  public void actualizar(Hecho hechoParaCambiar, Hecho hechoModificado) {
    this.eliminar(hechoParaCambiar);
    this.hechosRevisados.add(hechoModificado);
  }

  @Override
  public Set<Hecho> obtenerTodos() {
    Set<Hecho> copia = new HashSet<>(this.hechosRevisados);
    copia.addAll(this.hechosSinRevisar);
    return copia;
  }

  @Override
  public void agregar(Hecho hecho) {
    this.hechosSinRevisar.add(hecho);
  }

  @Override
  public void eliminar(Hecho hecho) {
    if (hechosRevisados.contains(hecho)) {
      this.hechosRevisados.remove(hecho);
    } else if (hechosSinRevisar.contains(hecho)) {
      this.hechosSinRevisar.remove(hecho);
    } else {
      throw new NoSePuedeEliminarUnHechoQueNoExisteException();
    }
  }

  @Override
  public void marcarComoRevisado(Hecho hecho) {
    this.hechosSinRevisar.remove(hecho);
    this.hechosRevisados.add(hecho);
  }

  @Override
  public Set<Hecho> obtenerNoRevisados() {
    return this.hechosSinRevisar;
  }
}
