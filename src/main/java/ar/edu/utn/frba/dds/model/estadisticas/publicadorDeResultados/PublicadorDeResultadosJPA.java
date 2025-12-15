package ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class PublicadorDeResultadosJPA implements PublicadorDeResultados, WithSimplePersistenceUnit {

  public PublicadorDeResultadosJPA() {}

  @Override
  public void comunicarResultados(List<ResultadoEstadistico> resultado) {
    resultado.forEach(resultadoEstadistico -> {entityManager().persist(resultadoEstadistico);});
  }
}
