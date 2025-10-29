package ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.time.LocalDateTime;
import java.util.List;

public interface ObjetoDeEstudio {

  List<ResultadoEstadistico> estudiar(LocalDateTime desde);
}
