package ar.edu.utn.frba.dds.persistencia;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

public class HechosDinamicosRepositoryTest implements SimplePersistenceTest {

  HechoRepository hechoRepository = new HechosFuenteDinamicaJPA();

  @Test
  public void marcaYObtieneRevisados() {
    Hecho primerHecho = new Hecho(
        null,
        "Incendio forestal",
        "Gran incendio en zona rural",
        "Desastre natural",
        -34.6037,
        -58.3816,
        LocalDate.of(2023, 12, 15),
        LocalDate.of(2023, 11, 30),
        Origen.DATASET);

    Hecho segundoHecho = new Hecho(
        null,
        "Incendio forestal",
        "Gran incendio en zona rural",
        "Desastre natural",
        -34.6037,
        -58.3816,
        LocalDate.of(2023, 12, 15),
        LocalDate.of(2023, 11, 30),
        Origen.DATASET);

    Hecho tercerHecho = new Hecho(
        null,
        "Incendio forestal",
        "Gran incendio en zona rural",
        "Desastre natural",
        -34.6037,
        -58.3816,
        LocalDate.of(2023, 12, 15),
        LocalDate.of(2023, 11, 30),
        Origen.DATASET);

    hechoRepository.agregar(primerHecho);
    hechoRepository.agregar(segundoHecho);
    hechoRepository.agregar(tercerHecho);

    hechoRepository.marcarComoRevisado(primerHecho);

    Set<Hecho> set = hechoRepository.obtenerNoRevisados();

    Assertions.assertEquals(2, set.size());
  }
}