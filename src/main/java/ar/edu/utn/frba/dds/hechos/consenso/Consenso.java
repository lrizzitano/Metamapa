package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.repositorios.FuentesRepository;
import ar.edu.utn.frba.dds.hechos.Hecho;
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
  private final FuentesRepository fuentes = FuentesRepository.instance();

  @OneToOne
  @JoinColumn(name = "algoritmoConsenso",nullable = false)
  private AlgoritmoConsenso algoritmoConsenso;

  @Transient //Es cache
  private Set<Hecho> hechosConsensuados = new HashSet<>();

  @Column(name = "proximaActualizacion")
  private LocalDate proximaActualizacion;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "consenso_id")
  private Long id;

  public Consenso(AlgoritmoConsenso algoritmoConsenso, LocalDate proximaActualizacion) {
    this.algoritmoConsenso = algoritmoConsenso;
    this.proximaActualizacion = proximaActualizacion;
  }

  public Consenso() {
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return proximaActualizacion.atStartOfDay().plusHours(3);
  }

  @Override
  public void actualizar() {
    this.hechosConsensuados = algoritmoConsenso.getHechosConsensuados(fuentes.getFuentes());
    this.proximaActualizacion = LocalDate.now().plusDays(1);
  }

  public Set<Hecho> getHechosConsensuados() {
    return this.hechosConsensuados;
  }

  public boolean esConsensuado(Hecho hecho) {
    return this.hechosConsensuados.contains(hecho);
  }
}
