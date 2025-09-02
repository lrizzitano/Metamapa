package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;

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
import java.util.Map;
import java.util.function.Predicate;

@Entity
@Table(name = "Filtro")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_filtro", discriminatorType = DiscriminatorType.STRING)
public abstract class Filtro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "filtro_id")
  public Long id;

  abstract public Predicate<Hecho> getAsPredicate();

  abstract public Map<String, String> toQueryParam();

}



