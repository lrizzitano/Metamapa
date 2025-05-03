package ar.edu.utn.frba.dds;

import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;


class FiltroTest {
  private final FiltroBuilder builder = new FiltroBuilder();
  private final Predicate<Hecho> siempreTrue = hecho -> true;
  private final Predicate<Hecho> siempreFalse = hecho -> false;
  private final Hecho hecho = mock(Hecho.class);


  @Test
  void filtroVacioSiempreCumpleParaAnd(){
    Predicate<Hecho> filtro = builder.obtenerFiltroAnd();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void filtroVacioSiempreCumpleParaOr(){
    Predicate<Hecho> filtro = builder.obtenerFiltroOr();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void trueMasFalseNoCumpleAnd(){
    builder.añadirFiltro(siempreTrue).añadirFiltro(siempreFalse);
    Predicate<Hecho>  filtro = builder.obtenerFiltroAnd();
    Assertions.assertFalse(filtro.test(hecho));
  }

  @Test
  void trueMasFalseCumpleOr(){
    builder.añadirFiltro(siempreTrue).añadirFiltro(siempreFalse);
    Predicate<Hecho>  filtro = builder.obtenerFiltroOr();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void trueMasTrueCumpleAnd(){
    builder.añadirFiltro(siempreTrue).añadirFiltro(siempreTrue);
    Predicate<Hecho>  filtro = builder.obtenerFiltroAnd();
    Assertions.assertTrue(filtro.test(hecho));
  }

  @Test
  void falseMasFalseNoCumpleOr(){
    builder.añadirFiltro(siempreFalse).añadirFiltro(siempreFalse);
    Predicate<Hecho>  filtro = builder.obtenerFiltroOr();
    Assertions.assertFalse(filtro.test(hecho));
  }
}
