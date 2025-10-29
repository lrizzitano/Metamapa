package ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.util.ArrayList;
import java.util.List;

public class PublicadorDeResultadosCompuesto implements PublicadorDeResultados {

  List<PublicadorDeResultados> publicadorDeResultados;

  PublicadorDeResultadosCompuesto(List<ResultadoEstadistico> resultado) {
    publicadorDeResultados = new ArrayList<PublicadorDeResultados>();
  }

  @Override
  public void comunicarResultados(List<ResultadoEstadistico> resultado) {
    publicadorDeResultados.forEach(publicadorDeResultado -> {publicadorDeResultado.comunicarResultados(resultado);});
  }
}
