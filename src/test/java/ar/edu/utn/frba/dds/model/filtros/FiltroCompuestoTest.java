package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class FiltroCompuestoTest {
  static class siempreFalse extends Filtro {
    public Predicate<Hecho> getAsPredicate() {
      return hecho -> false;
    }

    public Map<String,String> toQueryParam() {
      return new HashMap<>();
    }

    @Override
    public javax.persistence.criteria.Predicate toJpaPredicate(Root<Hecho> root, CriteriaBuilder cb) {
      return null;
    }

    @Override
    public String getNombre() {
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
  public void mapeaCorrectamente() {
    FiltroCompuesto compuesto = new FiltroCompuesto();
    Filtro filtro1 = new FiltroCategoria("hola");
    Filtro filtro2 = new FiltroFechaDesde(LocalDateTime.parse("2020-01-01T00:00"));
    compuesto.and(filtro1).and(filtro2);
    Map<String,String> mapa = new HashMap<>();
    mapa.put("categoria", "hola");
    mapa.put("fechaDesde", "2020-01-01T00:00");
    Assertions.assertEquals(mapa,
        compuesto.toQueryParam());
  }
}
