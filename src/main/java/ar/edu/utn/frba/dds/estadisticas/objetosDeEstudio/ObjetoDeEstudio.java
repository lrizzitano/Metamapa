package ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import java.util.List;

public interface ObjetoDeEstudio {

  List<ResultadoEstadistico> estudiar();

  // transformarDatosEnInformacion
  // calcularEstadisticas
  // calcularIndicadores, etc
}
