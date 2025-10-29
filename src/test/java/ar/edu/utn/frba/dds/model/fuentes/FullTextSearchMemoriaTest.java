package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FullTextSearchMemoriaTest implements SimplePersistenceTest {

  Fuente fuente;

  Hecho primerHecho = new Hecho(
      null,
      "Incendio forestal",
      "Gran incendio en zona rural",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho segundoHecho = new Hecho(
      null,
      "Incendio urbano",
      "Gran incendio en capital federal",
      "Desastre natural",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  Hecho tercerHecho = new Hecho(
      null,
      "represion policial",
      "represion en zona congreso",
      "Violencia estatal",
      new Ubicacion(-34.6037,
          -58.3816, null, null),
      LocalDate.of(2023, 12, 15).atStartOfDay(),
      LocalDate.of(2023, 11, 30).atStartOfDay(),
      Origen.DATASET);

  @BeforeEach
  public void setUp() {
    fuente = Mockito.spy(Fuente.class);
    when(fuente.obtenerHechos(any(Filtro.class))).thenReturn(
        new HashSet<Hecho>(Arrays.asList(primerHecho, segundoHecho, tercerHecho))
    );
  }

  @Test
  public void busquedaSinResultados() {
    Set<Hecho> accidentesTransito = fuente.obtenerHechos("vehiculo", new NullFiltro());

    Assertions.assertEquals(0, accidentesTransito.size());
  }

  @Test
  public void busquedaPorTitulo() {
    Set<Hecho> hechosForestales = fuente.obtenerHechos("forestal", new NullFiltro());

    Assertions.assertEquals(1, hechosForestales.size());
  }

  @Test
  public void busquedaPorDescripcion() {
    Set<Hecho> hechosRurales = fuente.obtenerHechos("rural", new NullFiltro());

    Assertions.assertEquals(1, hechosRurales.size());
  }

  @Test
  public void busquedaConMultiplesResultados() {

    Set<Hecho> incendios = fuente.obtenerHechos("incendio", new NullFiltro());
    Set<Hecho> policiales = fuente.obtenerHechos("policial", new NullFiltro());

    Assertions.assertEquals(2, incendios.size());
    Assertions.assertEquals(1, policiales.size());
  }

}
