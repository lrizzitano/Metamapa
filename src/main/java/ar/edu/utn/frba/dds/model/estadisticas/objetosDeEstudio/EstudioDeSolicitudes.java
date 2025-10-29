package ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudDeEliminacionRepository;
import java.time.LocalDateTime;
import java.util.List;

public class EstudioDeSolicitudes implements ObjetoDeEstudio{
  private final SolicitudDeEliminacionRepository repoSolicitudes;

  public EstudioDeSolicitudes(SolicitudDeEliminacionRepository repoSolicitudes) {
    this.repoSolicitudes = repoSolicitudes;
  }


  @Override
  public List<ResultadoEstadistico> estudiar(LocalDateTime desde) {
    return repoSolicitudes.getRechazadas().stream()
        .map(r -> (ResultadoEstadistico) r).toList();
  }
}
