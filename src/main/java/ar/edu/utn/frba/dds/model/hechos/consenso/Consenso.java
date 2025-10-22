package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.hechos.Hecho;
import org.hibernate.annotations.Cascade;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tieneConsenso",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("HayConsenso")
@Table(name ="Consenso")
public class Consenso implements Calendarizable {
  @Transient
  private FuentesRepository fuentes;

  @OneToOne
  @JoinColumn(name = "algoritmoConsenso",nullable = true)
  @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
  private AlgoritmoConsenso algoritmoConsenso;

  @Transient //Es cache
  private Set<Hecho> hechosConsensuados = new HashSet<>();

  @Column(name = "proximaActualizacion")
  @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
  private LocalDateTime proximaActualizacion;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
  @Column(name = "consenso_id")
  private Long id;

  public Consenso(AlgoritmoConsenso algoritmoConsenso, LocalDateTime proximaActualizacion, FuentesRepository fuentes) {
    this.algoritmoConsenso = algoritmoConsenso;
    this.proximaActualizacion = proximaActualizacion;
    this.fuentes = fuentes;
  }

  public Consenso() {
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return proximaActualizacion.withHour(3);
  }

  @Override
  public void actualizar() {
    this.hechosConsensuados = algoritmoConsenso.getHechosConsensuados(fuentes.obtenerFuentes());
    this.proximaActualizacion = LocalDateTime.now().plusDays(1);
  }

  public Set<Hecho> getHechosConsensuados() {
    return this.hechosConsensuados;
  }

  public boolean esConsensuado(Hecho hecho) {
    return this.hechosConsensuados.contains(hecho);
  }
}
