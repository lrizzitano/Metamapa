package ar.edu.utn.frba.dds;

import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;


class FiltroCompuestoBuilderTest {
  private FiltroCompuestoBuilder builder;
  private final Predicate<Hecho> siempreTrue = hecho -> true;
  private final Predicate<Hecho> siempreFalse = hecho -> false;
  private final Hecho hecho = mock(Hecho.class);

  @BeforeEach
  void setUp() {
    builder = new FiltroCompuestoBuilder();
  }

  @Test
  void filtroVacioSiempreCumple(){
    Predicate<Hecho> filtroAnd = builder.componerFiltrosAnd();
    Predicate<Hecho> filtroOr = builder.componerFiltrosOr();
    Assertions.assertTrue(filtroAnd.test(hecho));
    Assertions.assertTrue(filtroOr.test(hecho));
  }

  @Test
  void trueMasFalseNoCumpleAnd(){
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreFalse);
    Predicate<Hecho>  filtro = builder.componerFiltrosAnd();
    Assertions.assertFalse(filtro.test(hecho));
  }

  @Test
  void trueMasTrueCumpleAnd(){
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreTrue);
    Predicate<Hecho>  filtro = builder.componerFiltrosAnd();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void falseMasFalseNoCumpleOr() {
    builder.agregarFiltro(siempreFalse).agregarFiltro(siempreFalse);
    Predicate<Hecho> filtro = builder.componerFiltrosOr();
    Assertions.assertFalse(filtro.test(hecho));
  }

  @Test
  void trueMasFalseCumpleOr() {
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreFalse);
    Predicate<Hecho> filtro = builder.componerFiltrosOr();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void trueMasTrueCumpleOr() {
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreTrue);
    Predicate<Hecho> filtro = builder.componerFiltrosOr();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void seNiegaElFiltro(){
    builder.agregarFiltro(siempreTrue).negarFiltros();
    Predicate<Hecho> filtro = builder.componerFiltrosAnd();
    Assertions.assertFalse(filtro.test(hecho));
  }
}
