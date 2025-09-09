package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.calendarizables.Calendarizable;
import ar.edu.utn.frba.dds.estadisticas.exportador.PublicadorDeResultados;
import ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio.ObjetoDeEstudio;

import java.time.LocalDateTime;
import java.util.List;

public class Estadistico implements Calendarizable {

  private List<ObjetoDeEstudio> objetosDeEstudio;

  private PublicadorDeResultados publicador;

  public Estadistico(List<ObjetoDeEstudio> objetosDeEstudio, PublicadorDeResultados publicadorDeResultados) {
    this.objetosDeEstudio = objetosDeEstudio;
    this.publicador = publicadorDeResultados;
  }

  @Override
  public LocalDateTime proximaActualizacion() {
    return null;
  }

  @Override
  public void actualizar() {
    objetosDeEstudio.forEach(objetoDeEstudio -> {publicador.comunicarResultados(objetoDeEstudio.estudiar());});
    //actualziar fecha
  }
}
