package ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico;

import ar.edu.utn.frba.dds.model.hechos.Provincia;
import java.util.List;
import java.util.Map;
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

  public Long getCantHechos() {
    return cant_hechos;
  }

  // metodo utiliatario para quien quiera abrir una lista de hechos x provincia en un mapa de datos
  static Map<String, String> abrirProvincias(Map<String, String> datos, List<HechosPorProvincia> hechosXColecciones) {
    for (Provincia p : Provincia.values()) {
      datos.put(p.name(), "0");
    }
    if (hechosXColecciones != null) {
      for (HechosPorProvincia h : hechosXColecciones) {
        datos.put(h.getProvincia().name(), String.valueOf(h.getCantHechos()));
      }
    }

    return datos;
  }
}
