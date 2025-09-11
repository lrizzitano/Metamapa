package ar.edu.utn.frba.dds.persistencia;

import ar.edu.utn.frba.dds.execpciones.NoSePuedeEliminarUnHechoQueNoExisteException;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.hechos.Ubicacion;
import ar.edu.utn.frba.dds.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class HechosDinamicosRepositoryTest implements SimplePersistenceTest {

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
      LocalDateTime.of(2023, 11, 30,11,11),
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

}