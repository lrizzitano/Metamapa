package ar.edu.utn.frba.dds.model.calendarizables;

import ar.edu.utn.frba.dds.model.estadisticas.Estadistico;
import ar.edu.utn.frba.dds.model.estadisticas.RecolectorDeInformacion;
import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeColeccion;
import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeSolicitudes;
import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.ObjetoDeEstudio;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultados;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultadosJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDate;
import java.util.List;

public class ActualizadorEstadisticas implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new ActualizadorEstadisticas().actualizarEstadisticas();
  }

  private void actualizarEstadisticas() {
      PublicadorDeResultados publicadorBaseDeDatos = new PublicadorDeResultadosJPA();

      List<ObjetoDeEstudio> objetosDeEstudio = List.of(
          new EstudioDeCategoria(new RepoColecciones()),
          new EstudioDeColeccion(new RepoColecciones())
      );

      RecolectorDeInformacion recolectorDeInformacion = new RecolectorDeInformacion(objetosDeEstudio,
          publicadorBaseDeDatos);
      LocalDate fecha = new Estadistico().ultimaConsulta().orElse(LocalDate.of(2020, 1, 1));
      withTransaction(() -> recolectorDeInformacion.actualizar(fecha));
      recolectorDeInformacion.actualizar(fecha);
    }

}