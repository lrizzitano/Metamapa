package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.ObjetoDeEstudio;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultados;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecolectorDeInformacion {

  private List<ObjetoDeEstudio> objetosDeEstudio;
  private PublicadorDeResultados publicador;
  private LocalDateTime fecha;

  public RecolectorDeInformacion(List<ObjetoDeEstudio> objetosDeEstudio, PublicadorDeResultados publicadorDeResultados) {
    this.objetosDeEstudio = objetosDeEstudio;
    this.publicador = publicadorDeResultados;
    this.fecha = LocalDate.now().plusDays(1).atStartOfDay();
  }

  public LocalDateTime proximaActualizacion() {
    return fecha;
  }

  public void actualizar(LocalDateTime desde) {
    objetosDeEstudio.forEach(objetoDeEstudio -> {
      publicador.comunicarResultados(objetoDeEstudio.estudiar(desde));
    });
    this.fecha = fecha.plusDays(1);
  }
}
