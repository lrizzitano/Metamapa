package ar.edu.utn.frba.dds.estadisticas.publicadorDeResultados;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;

import java.util.List;

public interface PublicadorDeResultados {
  void comunicarResultados(List<ResultadoEstadistico> resultado);
}
