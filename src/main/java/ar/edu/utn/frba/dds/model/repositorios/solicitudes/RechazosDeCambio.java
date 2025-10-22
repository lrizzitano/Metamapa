package ar.edu.utn.frba.dds.repositorios.solicitudes;

import ar.edu.utn.frba.dds.hechos.Hecho;

import javax.persistence.*;

@Entity
public class RechazosDeCambio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Hecho hecho;

  @Column
  private Integer cantidad;

  public RechazosDeCambio(Hecho hecho, Integer cantidad) {
    this.hecho = hecho;
    this.cantidad = cantidad;
  }

  public RechazosDeCambio() {}

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
