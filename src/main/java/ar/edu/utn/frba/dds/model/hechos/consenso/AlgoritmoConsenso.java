package ar.edu.utn.frba.dds.model.hechos.consenso;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@Table(name="AlgoritmoConsenso")
public abstract class AlgoritmoConsenso {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "algoritmoConsenso_id")
  private Long id;

  abstract Set<Hecho> getHechosConsensuados(Set<Fuente> fuentes);

  public abstract String getNombre();
}
