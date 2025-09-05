package ar.edu.utn.frba.dds.repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import javax.persistence.EntityManager;

public class RepoGenerico<T> implements WithSimplePersistenceUnit {

  private final Class<T> entityClass;

  private final EntityManager entitityManager = entityManager();

  protected RepoGenerico(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public List<T> findAll() {
    return entitityManager.createQuery("from " + entityClass.getSimpleName(), entityClass)
        .getResultList();
  }

  public void save(T entity) {
    entitityManager.persist(entity);
  }

  public T update(T entity) {
    return entitityManager.merge(entity);
  }

  public void delete(T entity) {
    entitityManager.remove(entity);
  }
}