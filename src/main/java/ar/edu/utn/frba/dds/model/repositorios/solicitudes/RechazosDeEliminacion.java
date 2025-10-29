package ar.edu.utn.frba.dds.model.repositorios.solicitudes;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RechazosDeEliminacion implements ResultadoEstadistico {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String tituloHecho;

  @Column
  private Integer cantidadRechazadas;

  @Column
  private Integer cantidadSpam;

  public RechazosDeEliminacion(String tituloHecho, Integer cantidadRechazadas, Integer cantidadSpam) {
    this.tituloHecho = tituloHecho;
    this.cantidadRechazadas = cantidadRechazadas;
    this.cantidadSpam = cantidadSpam;
  }

  public RechazosDeEliminacion() {}

  public Long getId() {
    return id;
  }

  public String getTituloHecho() {
    return tituloHecho;
  }

  public Integer getCantidadRechazadas() {
    return cantidadRechazadas;
  }

  public Integer getCantidadSpam() {
    return cantidadSpam;
  }

  public void sumarRechazo() {
    this.cantidadRechazadas += 1;
  }

  public void sumarSpam() {
    this.cantidadSpam += 1;
  }

  @Override
  public Map<String, String> infoExportable() {
    return Map.of(
        "hecho", tituloHecho,
        "rechazos", cantidadRechazadas.toString(),
        "spam", cantidadSpam.toString()
    );
  }
}

