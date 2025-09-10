package ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.estadisticas.Provincia;

public class HechosPorProvincia {

  private Provincia provincia;
  private Long cant_hechos;

  public HechosPorProvincia(Provincia p, Long cant_hechos) {
    this.provincia = p;
    this.cant_hechos = cant_hechos;
  }

  public Provincia getProvincia() {
    return provincia;
  }

  public Long getCant_hechos() {
    return cant_hechos;
  }
}
