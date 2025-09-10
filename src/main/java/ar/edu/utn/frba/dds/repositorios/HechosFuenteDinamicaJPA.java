package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.HashSet;
import java.util.Set;

public class HechosFuenteDinamicaJPA implements HechoRepository, WithSimplePersistenceUnit {

  public HechosFuenteDinamicaJPA() {}

  @Override
  public void actualizar(Hecho hechoParaCambiar, Hecho hechoModificado) {
    hechoModificado.setId(hechoParaCambiar.id()); // reutiliza el id
    entityManager().merge(hechoModificado);
  }

  @Override
  public Set<Hecho> obtenerTodos() {
    return new HashSet<>(entityManager().createQuery("FROM Hecho", Hecho.class).getResultList());
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    return new HashSet<>(entityManager().createQuery("FROM Hecho", Hecho.class).getResultList());
  }

  @Override
  public void agregar(Hecho hecho) {
    entityManager().persist(hecho);
  }

  @Override
  public void eliminar(Hecho hecho) {

    if(hecho.id() == null) {
      throw new NoSePuedeEliminarUnHechoQueNoExisteException();
    }
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
