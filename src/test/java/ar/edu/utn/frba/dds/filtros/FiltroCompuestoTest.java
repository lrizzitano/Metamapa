package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class FiltroCompuestoTest {
  static class siempreFalse implements Filtro {
    public Predicate<Hecho> getAsPredicate() {
      return hecho -> false;
    }

    public Map<String,String> toQueryParam() {
      return new HashMap<>();
    }
  }

  @Test
  public void falseAndTrueEsFalse() {
    FiltroCompuesto compuesto = new FiltroCompuesto();
    compuesto.and(new NullFiltro()).and(new siempreFalse());
    Assertions.assertFalse(compuesto.getAsPredicate().test(mock(Hecho.class)));
  }

  @Test
  public void mapeaCorrectamente() {
    FiltroCompuesto compuesto = new FiltroCompuesto();
    Filtro filtro1 = new FiltroCategoria("hola");
    Filtro filtro2 = new FiltroCategoria("chau");
    compuesto.and(filtro1).and(filtro2);
    Map<String,String> mapa = new HashMap<>();
    mapa.put(filtro1.toString(), "hola");
    mapa.put(filtro2.toString(), "chau");
    Assertions.assertEquals(mapa,
        compuesto.toQueryParam());
  }
}
