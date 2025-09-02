package ar.edu.utn.frba.dds.hechos.repositorios;

import ar.edu.utn.frba.dds.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.HashSet;
import java.util.Set;

public class HechosDinamicosRepository implements HechoRepository, WithSimplePersistenceUnit {

  public HechosDinamicosRepository() {}

  @Override
  public void actualizar(Hecho hechoParaCambiar, Hecho hechoModificado) {
    entityManager().merge(hechoModificado);
  }

  @Override
  public Set<Hecho> obtenerTodos() {
    return new HashSet<>(entityManager().createQuery("FROM Hecho", Hecho.class).getResultList());
  }

  @Override
  public void agregar(Hecho hecho) {
    entityManager().persist(hecho);
  }

  @Override
  public void eliminar(Hecho hecho) {
    entityManager().remove(hecho);
  }

  @Override
  public void marcarComoRevisado(Hecho hecho) {
    entityManager().createQuery(
             "UPDATE Hecho h " +
                "SET h.fueRevisado = :fueRevisado " +
                "WHERE h.id = :hecho_id")
        .setParameter("hecho_id", hecho.id())
        .setParameter("fueRevisado", true)
        .executeUpdate();
  }

  @Override
  public Set<Hecho> obtenerNoRevisados() {

    return new HashSet<>(
        entityManager().createQuery(
                     "FROM Hecho h " +
                        "WHERE h.fueRevisado = :fueRevisado ", Hecho.class)
                    .setParameter("fueRevisado", false)
                    .getResultList()
    );
  }
}
