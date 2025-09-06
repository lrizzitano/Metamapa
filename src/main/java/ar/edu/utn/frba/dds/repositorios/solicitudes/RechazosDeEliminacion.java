package ar.edu.utn.frba.dds.repositorios.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RechazosDeEliminacion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Hecho hecho;

  @Column
  private Integer cantidad;

  public RechazosDeEliminacion(Hecho hecho, Integer cantidad) {
    this.hecho = hecho;
    this.cantidad = cantidad;
  }

  public RechazosDeEliminacion() {}

  public Long getId() {
    return id;
  }

  public Hecho getHecho() {
    return hecho;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void sumarRechazo() {
    this.cantidad += 1;
  }

}

