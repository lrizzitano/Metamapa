package ar.edu.utn.frba.dds.model.repositorios;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashSet;
import java.util.Set;

public class FuentesRepositoryJPA implements FuentesRepository, WithSimplePersistenceUnit {
  public Set<Fuente> obtenerFuentes() {
    return new HashSet<>(
        entityManager().createQuery("from Fuente ", Fuente.class)
        .getResultList()
    );
  }

  public void agregarFuente(Fuente entity) {
    entityManager().persist(entity);
  }

  public void eliminarFuente(Fuente entity) {
    entityManager().remove(entity);
  }

  public Fuente find(Long id){
    return entityManager().find(Fuente.class, id);
  }
}
