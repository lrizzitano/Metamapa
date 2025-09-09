package ar.edu.utn.frba.dds.estadisticas.exportador;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;

import java.util.List;

public interface PublicadorDeResultados {
  void comunicarResultados(List<ResultadoEstadistico> resultado);
}
