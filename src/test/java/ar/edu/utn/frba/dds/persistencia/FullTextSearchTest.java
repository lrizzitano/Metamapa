package ar.edu.utn.frba.dds.persistencia;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.hechos.Ubicacion;
import ar.edu.utn.frba.dds.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class FullTextSearchTest implements SimplePersistenceTest {

  HechoRepository hechosFuenteDinamica;
  Usuario usuario = new Usuario("Peperino", "Pomoro", 43);

  @BeforeEach
  public void setUp() {
    hechosFuenteDinamica = new HechosFuenteDinamicaJPA();
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
  void fullTextSearchMySQL() {

    hechosFuenteDinamica.agregar(primerHecho);
    hechosFuenteDinamica.agregar(segundoHecho);
    hechosFuenteDinamica.agregar(tercerHecho);

    entityManager().flush();
    entityManager().getTransaction().commit();
    entityManager().getTransaction().begin();

    List<Long> ids = List.of(primerHecho.id(), segundoHecho.id(), tercerHecho.id());

    Assertions.assertEquals(3,hechosFuenteDinamica.obtenerTodos().size());

    Set<Hecho> incendios = hechosFuenteDinamica.fullTextSearch("incendio");
    Set<Hecho> policiales = hechosFuenteDinamica.fullTextSearch("policial");

    Assertions.assertEquals(2,incendios.size());
    Assertions.assertEquals(1,policiales.size());

    entityManager().createQuery("DELETE FROM Hecho h WHERE h.id IN :ids")
        .setParameter("ids", ids)
        .executeUpdate();

    entityManager().flush();
    entityManager().getTransaction().commit();
  }

  @Test
  public void fullTextSearchMemoria() {
    Fuente fuente = Mockito.spy(Fuente.class);
    when(fuente.obtenerHechos(any(Filtro.class))).thenReturn(
        new HashSet<Hecho>(Arrays.asList(primerHecho, segundoHecho, tercerHecho))
    );

    Assertions.assertEquals(3,fuente.obtenerHechos(new NullFiltro()).size());

    Set<Hecho> incendios = fuente.obtenerHechos("incendio", new NullFiltro());
    Set<Hecho> policiales = fuente.obtenerHechos("policial", new NullFiltro());

    Assertions.assertEquals(2, incendios.size());
    Assertions.assertEquals(1, policiales.size());
  }

}
