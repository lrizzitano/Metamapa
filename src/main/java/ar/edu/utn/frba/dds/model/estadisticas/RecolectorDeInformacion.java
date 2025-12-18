package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.ObjetoDeEstudio;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultados;
import java.time.LocalDate;
import java.util.List;

public class RecolectorDeInformacion  {

  private final List<ObjetoDeEstudio> objetosDeEstudio;
  private final PublicadorDeResultados publicador;

  public RecolectorDeInformacion(List<ObjetoDeEstudio> objetosDeEstudio, PublicadorDeResultados publicadorDeResultados) {
    this.objetosDeEstudio = objetosDeEstudio;
    this.publicador = publicadorDeResultados;
  }

  public void actualizar(LocalDate ultima) {
    objetosDeEstudio.forEach(objetoDeEstudio -> {
      for(var step = ultima; !step.equals(LocalDate.now()); step = step.plusDays(1)) {
        publicador.comunicarResultados(objetoDeEstudio.estudiar(step.atStartOfDay()));
      }
    });
  }
}
