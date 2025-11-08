package ar.edu.utn.frba.dds.model.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class RepoGenerico<T> implements WithSimplePersistenceUnit {

  private final Class<T> entityClass;

  protected RepoGenerico(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public Set<T> findAll() {
    return entityManager().createQuery("from " + entityClass.getSimpleName(), entityClass)
        .getResultStream().collect(Collectors.toSet());
  }

  public T find(Long id) {
    return entityManager().find(entityClass, id);
  }

  public void save(T entity) {
    entityManager().persist(entity);
    System.out.println(entityManager());
  }

  public T update(T entity) {
    return entityManager().merge(entity);
  }

  public void delete(T entity) {
    entityManager().remove(entity);
  }
}