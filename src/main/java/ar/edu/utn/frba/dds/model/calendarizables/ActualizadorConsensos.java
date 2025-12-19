package ar.edu.utn.frba.dds.model.calendarizables;

import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensosRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class ActualizadorConsensos implements WithSimplePersistenceUnit {

  public static void main(String[] args) {
    new ActualizadorConsensos().run();
  }

  private void run() {
    try {
        ConsensosRepository consensos = new ConsensosRepository();

        consensos.findAll().forEach(c -> {
          c.setFuentesRepository(new FuentesRepositoryJPA());
          c.actualizar();
          withTransaction(() -> {
            consensos.update(c);
        });
      });
    } catch (Exception e) {
      new Logger().info("Error actualizando consensos: " + e.getMessage());
    }
  }
}
