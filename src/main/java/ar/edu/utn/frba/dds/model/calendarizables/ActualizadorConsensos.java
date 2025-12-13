package ar.edu.utn.frba.dds.model.calendarizables;

import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensosRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.server.configuracion.Logger;

public class ActualizadorConsensos {
  public static void main(String[] args) {
    try {
      ConsensosRepository consensos = new ConsensosRepository();

      consensos.findAll().forEach(c-> {
        c.setFuentesRepository(new FuentesRepositoryJPA());
        c.actualizar();
        consensos.update(c);
      });
    }
    catch (Exception e) {
      new Logger().info("Error actualizando consensos: " + e.getMessage());
    }
  }
}

