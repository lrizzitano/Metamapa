package ar.edu.utn.frba.dds.model.persistencia;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Agregador;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FullTextSearchColeccionTest implements SimplePersistenceTest {

  Fuente fuenteMemoria;
  FuenteDinamica fuenteDB = FuenteDinamica.instance();
  RepoUsuarios repoUsuarios;
  Agregador agregador;
  HechoRepository hechosFuenteDinamica;
  List<Long> ids;

  @BeforeEach
  public void setUp() {
    hechosFuenteDinamica = new HechosFuenteDinamicaJPA();
    fuenteDB.setHechoRepository(hechosFuenteDinamica);

    RepoUsuarios repoUsuarios = new RepoUsuarios();
    repoUsuarios.save(contribuyente);

    for (Hecho hecho : Arrays.asList(primerHecho, segundoHecho, tercerHecho)) {
      hecho.setContribuyente(contribuyente);
      fuenteDB.agregarHecho(hecho);
    }

    entityManager().flush();
    entityManager().getTransaction().commit();
    entityManager().getTransaction().begin();

    ids = List.of(primerHecho.id(), segundoHecho.id(), tercerHecho.id());

    fuenteMemoria = Mockito.spy(Fuente.class);
    when(fuenteMemoria.obtenerHechos(any(Filtro.class))).thenReturn(
        new HashSet<Hecho>(Arrays.asList(cuartoHecho, quintoHecho, sextoHecho))
    );

    Set<Fuente> fuentes = new HashSet<>(Arrays.asList(fuenteDB, fuenteMemoria));
    agregador = new Agregador(fuentes, LocalDateTime.now().plusHours(1), Duration.ofHours(1));
    agregador.actualizar();
  }

  @AfterEach
  public void tearDown() {
    entityManager().createQuery("DELETE FROM Hecho h WHERE h.id IN :ids")
        .setParameter("ids", ids)
        .executeUpdate();

    entityManager().flush();
    entityManager().getTransaction().commit();

  }

  Usuario contribuyente = new Usuario("Lulii", "Luli", "Ofman", LocalDate.now(), "a");

  // === Hechos guardados en DB ===

  Hecho primerHecho = new Hecho(
      null,
      "Incendio forestal",
      "Tragico incendio en zona rural",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho segundoHecho = new Hecho(
      null,
      "Accidente por vehiculo en via de tren",
      "Tren Urquiza levanta en pala automovil, sin heridos",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho tercerHecho = new Hecho(
      null,
      "Choque en panamericana",
      "Gran accidente entre vehiculo de pasajeros y auto",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  // === Hechos guardados en memoria ===

  Hecho cuartoHecho = new Hecho(
      null,
      "represion policial",
      "represion en zona congreso",
      "Violencia estatal",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho  quintoHecho = new Hecho(
      null,
      "Incendio urbano",
      "Gran incendio en capital federal",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho sextoHecho = new Hecho(
      null,
      "Joven se cae solo de la bici",
      "Accidente con vehiculo traccionado a sangre, 7 muertos",
      "Violencia estatal",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  @Test
  public void busquedaSinResultados() {
    Set<Hecho> hechosExplosivos = agregador.obtenerHechos("explosion", new NullFiltro());

    Assertions.assertEquals(0, hechosExplosivos.size());
  }

  @Test
  public void busquedaPorTitulo() {
    Set<Hecho> incendios = agregador.obtenerHechos("incendio", new NullFiltro());

    Assertions.assertEquals(2, incendios.size());
  }

  @Test
  public void busquedaPorDescripcion() {
    Set<Hecho> hechosGrandes = agregador.obtenerHechos("gran", new NullFiltro());

    Assertions.assertEquals(2, hechosGrandes.size());
  }

  @Test
  public void busquedaConMultiplesResultados() {

    Set<Hecho> hechosVehiculares = agregador.obtenerHechos("vehiculo", new NullFiltro());

    Assertions.assertEquals(3, hechosVehiculares.size());
  }


}
