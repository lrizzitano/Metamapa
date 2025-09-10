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

  @Column
  private String tituloHecho;

  @Column
  private Integer cantidad;

  public RechazosDeEliminacion(String tituloHecho, Integer cantidad) {
    this.tituloHecho = tituloHecho;
    this.cantidad = cantidad;
  }

  public RechazosDeEliminacion() {}

  public Long getId() {
    return id;
  }

  public String getTituloHecho() {
    return tituloHecho;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void sumarRechazo() {
    this.cantidad += 1;
  }

}

