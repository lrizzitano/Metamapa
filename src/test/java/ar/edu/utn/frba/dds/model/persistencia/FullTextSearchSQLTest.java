package ar.edu.utn.frba.dds.model.persistencia;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FullTextSearchSQLTest implements SimplePersistenceTest {

  HechoRepository hechosFuenteDinamica;
  List<Long> ids;

  @BeforeEach
  public void setUp() {
    hechosFuenteDinamica = new HechosFuenteDinamicaJPA();

    hechosFuenteDinamica.agregar(primerHecho);
    hechosFuenteDinamica.agregar(segundoHecho);
    hechosFuenteDinamica.agregar(tercerHecho);

    entityManager().flush();
    entityManager().getTransaction().commit();
    entityManager().getTransaction().begin();

    ids = List.of(primerHecho.id(), segundoHecho.id(), tercerHecho.id());
  }

  @AfterEach
  public void tearDown() {
    entityManager().createQuery("DELETE FROM Hecho h WHERE h.id IN :ids")
        .setParameter("ids", ids)
        .executeUpdate();

    entityManager().flush();
    entityManager().getTransaction().commit();

  }

  Hecho primerHecho = new Hecho(
      null,
      "Incendio forestal",
      "Gran incendio en zona rural",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho segundoHecho = new Hecho(
      null,
      "Incendio urbano",
      "Gran incendio en capital federal",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho tercerHecho = new Hecho(
      null,
      "represion policial",
      "represion en zona congreso",
      "Violencia estatal",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  @Test
  void busquedaSinResultados() {
    Assertions.assertEquals(3, hechosFuenteDinamica.obtenerTodos().size());

    Set<Hecho> accidentesTransito = hechosFuenteDinamica.fullTextSearch("vehiculo");

    Assertions.assertEquals(0, accidentesTransito.size());
  }

  @Test
  void busquedaPorTitulo() {
    Assertions.assertEquals(3, hechosFuenteDinamica.obtenerTodos().size());

    Set<Hecho> hechosForestales = hechosFuenteDinamica.fullTextSearch("forestal");

    Assertions.assertEquals(1, hechosForestales.size());
  }

  @Test
  void busquedaPorDescripcion() {
    Assertions.assertEquals(3, hechosFuenteDinamica.obtenerTodos().size());

    Set<Hecho> hechosRurales = hechosFuenteDinamica.fullTextSearch("rural");

    Assertions.assertEquals(1, hechosRurales.size());
  }

  @Test
  void busquedaConMultiplesResultados() {
    List<Long> ids = List.of(primerHecho.id(), segundoHecho.id(), tercerHecho.id());

    Assertions.assertEquals(3, hechosFuenteDinamica.obtenerTodos().size());

    Set<Hecho> incendios = hechosFuenteDinamica.fullTextSearch("incendio");
    Set<Hecho> policiales = hechosFuenteDinamica.fullTextSearch("policial");

    Assertions.assertEquals(2, incendios.size());
    Assertions.assertEquals(1, policiales.size());
  }

}
