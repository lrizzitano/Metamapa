package ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.util.List;

public interface PublicadorDeResultados {
  void comunicarResultados(List<ResultadoEstadistico> resultado);
}
