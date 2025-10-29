package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.ObjetoDeEstudio;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultados;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecolectorDeInformacion implements Calendarizable {

  private List<ObjetoDeEstudio> objetosDeEstudio;
  private PublicadorDeResultados publicador;
  private LocalDateTime fecha;

  public RecolectorDeInformacion(List<ObjetoDeEstudio> objetosDeEstudio, PublicadorDeResultados publicadorDeResultados) {
    this.objetosDeEstudio = objetosDeEstudio;
    this.publicador = publicadorDeResultados;
    this.fecha = LocalDate.now().plusDays(1).atStartOfDay();
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return fecha;
  }

  @Override
  public void actualizar() {
    objetosDeEstudio.forEach(objetoDeEstudio -> {publicador.comunicarResultados(objetoDeEstudio.estudiar(LocalDateTime.now().minusDays(1)));});
    this.fecha = fecha.plusDays(1);
  }
}
