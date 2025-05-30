package ar.edu.utn.frba.dds;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.Filtros.Filtro;
import ar.edu.utn.frba.dds.Fuentes.Fuente;
import ar.edu.utn.frba.dds.Hechos.Coleccion;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Hechos.Origen;
import ar.edu.utn.frba.dds.Solicitudes.Solicitudes;
import ar.edu.utn.frba.dds.Usuarios.Administrador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ColeccionTest {
  private final Hecho hecho1 = new Hecho("hecho1", "desc1", "cat1",
      1.0, 2.0,  LocalDate.now(), LocalDate.parse("2024-01-01"), Origen.DATASET);
  private final Hecho hecho2 = new Hecho("hecho2", "desc2", "cat2",
      3.0,4.0, LocalDate.now(), LocalDate.parse("2024-01-02"), Origen.DATASET);
  private final Fuente unaFuente = mock(Fuente.class);


  @BeforeEach
  void setUp() {
    Set<Hecho> hechos = Set.of(hecho1, hecho2);
    when(unaFuente.obtenerHechos(any())).thenAnswer(invocation -> {
      Predicate<Hecho> filtro = invocation.getArgument(0);
      return hechos.stream().filter(filtro).collect(Collectors.toSet());
    });
  }

  @AfterEach
  void tearDown() {
    Solicitudes.instance().reset();
  }

  @Test
  void ColeccionFiltraPorCriterioDePertenencia() {
    Coleccion unaColeccion = new Coleccion("", "",
        Filtro.CATEGORIA.crearFiltro("cat1"), unaFuente);
    Assertions.assertEquals(unaColeccion.hechos(hecho -> true), Set.of(hecho1));
  }

  @Test
  void ColeccionFiltraPorParametro() {
    Coleccion unaColeccion = new Coleccion("", "", hecho -> true, unaFuente);
    Assertions.assertEquals(unaColeccion.hechos(Filtro.CATEGORIA.crearFiltro("cat1")),
        Set.of(hecho1));
  }

  @Test
  void CollecionFiltraHechosEliminados() {
    Solicitud unaSolicitud = new Solicitud(hecho1, "");
    unaSolicitud.aceptar(mock(Administrador.class));
    Coleccion unaColeccion = new Coleccion("", "", hecho -> true, unaFuente);
    Assertions.assertEquals(unaColeccion.hechos(hecho -> true), Set.of(hecho2));
  }
}