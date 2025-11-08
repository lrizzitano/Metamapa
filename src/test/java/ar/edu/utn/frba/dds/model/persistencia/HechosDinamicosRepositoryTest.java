package ar.edu.utn.frba.dds.model.persistencia;

import ar.edu.utn.frba.dds.model.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroCompuesto;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaDesde;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HechosDinamicosRepositoryTest implements SimplePersistenceTest {

  HechoRepository hechosFuenteDinamica;
  Usuario usuario = new Usuario("pepe", "Peperino","Pomoro", LocalDate.now(), "a");

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
      LocalDateTime.of(2023, 12, 15,11,11),
      LocalDateTime.of(2023, 11, 30,11,11),
      Origen.DATASET);

  Hecho segundoHecho = new Hecho(
      null,
      "Incendio urbano",
      "Gran incendio en capital federal",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDateTime.of(2023, 12, 15,11,11),
      LocalDateTime.of(2022, 11, 30,11,11),
      Origen.DATASET);

  Hecho tercerHecho = new Hecho(
      null,
      "represion policial",
      "represion en zona congreso",
      "Violencia estatal",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDateTime.of(2023, 12, 15,11,11),
      LocalDateTime.of(2023, 11, 30,11,11),
      Origen.DATASET);


  @Test
  public void marcaYObtieneRevisados() {

    hechosFuenteDinamica.agregar(primerHecho);
    hechosFuenteDinamica.agregar(segundoHecho);
    hechosFuenteDinamica.agregar(tercerHecho);

    hechosFuenteDinamica.marcarComoRevisado(primerHecho);

    Set<Hecho> set = hechosFuenteDinamica.obtenerNoRevisados();

    Assertions.assertEquals(2, set.size());
  }

  @Test
  void ActualizarHechoEliminaElQueEstabaYAgregaElNuevo()
  {

    hechosFuenteDinamica.agregar(primerHecho);

    Assertions.assertNotNull(primerHecho.id());

    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(primerHecho));

    hechosFuenteDinamica.actualizar(primerHecho,segundoHecho);

    Set<Hecho> hechosActualizados = hechosFuenteDinamica.obtenerTodos();

    Hecho hechoPersistido = hechosActualizados.iterator().next();

    Assertions.assertEquals(1,hechosActualizados.size());
    Assertions.assertEquals(primerHecho.id(),segundoHecho.id());
    Assertions.assertEquals("Incendio urbano",hechoPersistido.titulo());
  }

  @Test
  void NoPuedoEliminarHechoNoContenidoEnElSet() {

    Assertions.assertThrows(NoSePuedeEliminarUnHechoQueNoExisteException.class, () -> {
      hechosFuenteDinamica.eliminar(primerHecho);
    });
  }

  @Test
  void agregarHechoLoSumaAlSet() {

    hechosFuenteDinamica.agregar(primerHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(primerHecho));
  }

  @Test
  void eliminarHechoLoSacaDelSet() {

    hechosFuenteDinamica.agregar(primerHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerTodos().contains(primerHecho));

    hechosFuenteDinamica.eliminar(primerHecho);
    Assertions.assertFalse(hechosFuenteDinamica.obtenerTodos().contains(primerHecho));
  }

  @Test
  void unHechoArrancaComoNoRevisado() {
    hechosFuenteDinamica.agregar(primerHecho);
    Assertions.assertTrue(hechosFuenteDinamica.obtenerNoRevisados().contains(primerHecho));
  }

  @Test
  void sePuedeMarcarComoRevisadoUnHecho() {
    hechosFuenteDinamica.agregar(primerHecho);
    hechosFuenteDinamica.marcarComoRevisado(primerHecho);
    Assertions.assertFalse(hechosFuenteDinamica.obtenerNoRevisados().contains(primerHecho));
  }

  @Test
  void filtraEnSql() {
    hechosFuenteDinamica.agregar(primerHecho);
    hechosFuenteDinamica.agregar(segundoHecho);
    hechosFuenteDinamica.agregar(tercerHecho);

    var hechos = hechosFuenteDinamica.obtenerHechos(new FiltroCategoria("Desastre natural"));

    Assertions.assertEquals(Set.of(primerHecho, segundoHecho), hechos);
  }

  @Test
  void filtraCompuestoEnSql() {
    hechosFuenteDinamica.agregar(primerHecho);
    hechosFuenteDinamica.agregar(segundoHecho);
    hechosFuenteDinamica.agregar(tercerHecho);

    var hechos = hechosFuenteDinamica.obtenerHechos(
        new FiltroCompuesto(List.of(
            new FiltroCategoria("Desastre natural"),
            new FiltroFechaDesde(LocalDateTime.of(2023,10,1,0,0))))
    );

    Assertions.assertEquals(Set.of(primerHecho), hechos);
  }

}