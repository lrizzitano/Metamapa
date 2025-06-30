package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class FuenteDemoTest {

  private static class TestConexion implements Conexion {
    private final Queue<Map<String, Object>> queue = new LinkedList<>();

    void addRow(Map<String, Object> row) {
      queue.add(row);
    }

    @Override
    public Map<String, Object> siguienteHecho(URL url, Instant ultimaConsulta) {
      return queue.poll();
    }
  }

  private TestConexion conexion;
  private FuenteDemo fuente;

  @BeforeEach
  void setUp() {
    conexion = new TestConexion();
    fuente = new FuenteDemo(conexion, null);
  }

  @Test
  void acumulaHechosSoloTrasActualizar() {
    Map<String, Object> uno  = row("uno",  "cat1");
    Map<String, Object> dos  = row("dos",  "cat2");

    conexion.addRow(uno);
    fuente.actualizar();
    Assertions.assertEquals(Set.of("cat1"), categorias(fuente.obtenerHechos(new NullFiltro())));
    conexion.addRow(dos);
    Assertions.assertEquals(1, fuente.obtenerHechos(new NullFiltro()).size());
    fuente.actualizar();
    Assertions.assertEquals(Set.of("cat1", "cat2"), categorias(fuente.obtenerHechos(new NullFiltro())));
  }

  @Test
  void filtraPorCategoria() {
    conexion.addRow(row("a", "hola"));
    conexion.addRow(row("b", "otra"));
    fuente.actualizar();

    Assertions.assertEquals(Set.of("hola"), categorias(fuente.obtenerHechos(new FiltroCategoria("hola"))));
  }

  @Test
  void noSiempreEsActualizable() {
    fuente.actualizar();
    Assertions.assertFalse(fuente.tocaActualizar());
  }

  //Aux
  private Map<String, Object> row(String titulo, String categoria) {
    Map<String, Object> m = new HashMap<>();
    m.put("titulo", titulo);
    m.put("descripcion", "desc " + titulo);
    m.put("categoria", categoria);
    m.put("latitud",  0.0);
    m.put("longitud", 0.0);
    m.put("fecha", LocalDate.now());
    return m;
  }

  private Set<String> categorias(Set<Hecho> hechos) {
    return hechos.stream().map(Hecho::categoria).collect(Collectors.toSet());
  }
}
