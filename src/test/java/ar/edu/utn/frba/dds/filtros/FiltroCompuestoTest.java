package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class FiltroCompuestoTest {
  static class siempreFalse implements Filtro {
    public Predicate<Hecho> getAsPredicate() {
      return hecho -> false;
    }

    public String toHttp() {
      return "";
    }
  }

  @Test
  public void falseAndTrueEsFalse() {
    FiltroCompuesto compuesto = new FiltroCompuesto();
    compuesto.and(new NullFiltro()).and(new siempreFalse());
    Assertions.assertFalse(compuesto.getAsPredicate().test(mock(Hecho.class)));
  }

  @Test
  public void concatenaCorrectamente() {
    FiltroCompuesto compuesto = new FiltroCompuesto();
    Filtro filtro1 = new FiltroCategoria("hola");
    Filtro filtro2 = new FiltroCategoria("chau");
    compuesto.and(filtro1).and(filtro2);
    Assertions.assertEquals(filtro1.toHttp()+"&"+filtro2.toHttp(), compuesto.toHttp());
  }

  @Test
  public void noConcatenaVacios(){
    FiltroCompuesto compuesto = new FiltroCompuesto();
    Filtro filtro = new FiltroCategoria("hola");
    compuesto.and(filtro).and(new siempreFalse());
    Assertions.assertEquals(filtro.toHttp(), compuesto.toHttp());
  }
}
