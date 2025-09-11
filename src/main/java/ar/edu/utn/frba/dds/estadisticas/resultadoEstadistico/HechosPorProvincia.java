package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.hechos.Provincia;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class HechosPorProvincia {

  @Column(name = "provincia")
  @Enumerated(EnumType.STRING)
  private Provincia provincia;
  @Column(name = "hechos_de_provincia")
  private Long cant_hechos;

  public HechosPorProvincia(Provincia p, Long cant_hechos) {
    this.provincia = p;
    this.cant_hechos = cant_hechos;
  }
  public HechosPorProvincia() {

  }

  public Provincia getProvincia() {
    return provincia;
  }

  public Long getCant_hechos() {
    return cant_hechos;
  }
}
