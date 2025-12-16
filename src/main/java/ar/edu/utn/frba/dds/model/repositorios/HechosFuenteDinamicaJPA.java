package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HechosFuenteDinamicaJPA implements HechoRepository, WithSimplePersistenceUnit {

  public HechosFuenteDinamicaJPA() {}

  @Override
  public void actualizar(Hecho hechoParaCambiar, Hecho hechoModificado) {
    hechoParaCambiar.setDescripcion(hechoModificado.getDescripcion());
    hechoParaCambiar.setCategoria(hechoModificado.getCategoria());
    hechoParaCambiar.setUbicacion(hechoModificado.getUbicacion());
    hechoParaCambiar.setFechaCarga(hechoModificado.getFechaCarga());
    hechoParaCambiar.setFechaAcontecimiento(hechoModificado.getFechaAcontecimiento());
    hechoParaCambiar.setOrigen(hechoModificado.getOrigen());
    hechoParaCambiar.setContribuyente(hechoModificado.getContribuyente());
    hechoParaCambiar.setFueRevisado(hechoModificado.getFueRevisado());
    hechoParaCambiar.setImagen(hechoModificado.getImagen());
    hechoParaCambiar.setVideo(hechoModificado.getVideo());

    entityManager().merge(hechoParaCambiar);
  }

  @Override
  public Set<Hecho> obtenerTodos() {
    return new HashSet<>(entityManager().createQuery("FROM Hecho", Hecho.class).getResultList());
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    CriteriaBuilder cb = entityManager().getCriteriaBuilder();
    CriteriaQuery<Hecho> query = cb.createQuery(Hecho.class);
    Root<Hecho> root = query.from(Hecho.class);
    query.select(root).where(filtro.toJpaPredicate(root, cb));

    return new HashSet<>(entityManager().createQuery(query).getResultList());
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

  @SuppressWarnings("unchecked")
  public Set<Hecho> fullTextSearch(String texto) {
    return new HashSet<>(
        entityManager()
            .createNativeQuery(
                "SELECT * FROM Hecho h " +
                    "WHERE MATCH(h.titulo, h.descripcion) AGAINST (:texto IN BOOLEAN MODE)",
                Hecho.class
            )
            .setParameter("texto", texto)
            .getResultList()
    );
  }

}
