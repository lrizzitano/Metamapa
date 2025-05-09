package ar.edu.utn.frba.dds;

import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;


class FiltroCompuestoBuilderTest {
  private final FiltroCompuestoBuilder builder = new FiltroCompuestoBuilder();
  private final Predicate<Hecho> siempreTrue = hecho -> true;
  private final Predicate<Hecho> siempreFalse = hecho -> false;
  private final Hecho hecho = mock(Hecho.class);

  @BeforeEach
  void setUp() {
    builder.reset();
  }

  @Test
  void filtroVacioSiempreCumpleParaAnd(){
    Predicate<Hecho> filtro = builder.componerFiltros();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void trueMasFalseNoCumpleAnd(){
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreFalse);
    Predicate<Hecho>  filtro = builder.componerFiltros();
    Assertions.assertFalse(filtro.test(hecho));
  }

  @Test
  void trueMasTrueCumpleAnd(){
    builder.agregarFiltro(siempreTrue).agregarFiltro(siempreTrue);
    Predicate<Hecho>  filtro = builder.componerFiltros();
    Assertions.assertTrue(filtro.test(hecho));
  }
}
