package ar.edu.utn.frba.dds;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class FiltroTest {

  private final Hecho hecho = new Hecho(
    "Incendio forestal",
    "Gran incendio en zona rural",
    "Desastre natural",
    -34.6037,
    -58.3816,
    LocalDate.of(2023, 12, 15),
    LocalDate.of(2023, 11, 30),
    Origen.DATASET);

  @Test
  void tituloContieneTexto() {
    Predicate<Hecho> filtro = Filtro.TITULO.crearFiltro("incendio");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void descripcionContieneTexto() {
    Predicate<Hecho> filtro = Filtro.DESCRIPCION.crearFiltro("zona");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void categoriaContieneTexto() {
    Predicate<Hecho> filtro = Filtro.CATEOGIRA.crearFiltro("desastre");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void latitudMayorQueValor() {
    Predicate<Hecho> filtro = Filtro.LATITUD_MAYOR.crearFiltro("-35");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void latitudMenorQueValor() {
    Predicate<Hecho> filtro = Filtro.LATITUD_MENOR.crearFiltro("-30");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void longitudMayorQueValor() {
    Predicate<Hecho> filtro = Filtro.LONGITUD_MAYOR.crearFiltro("-60");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void longitudMenorQueValor() {
    Predicate<Hecho> filtro = Filtro.LONGITUD_MENOR.crearFiltro("-50");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void fechaCargaAntesDe() {
    Predicate<Hecho> filtro = Filtro.FECHA_CARGA_ANTES.crearFiltro("2023-12-31");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void fechaCargaDespuesDe() {
    Predicate<Hecho> filtro = Filtro.FECHA_CARGA_DESPUES.crearFiltro("2023-01-01");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void fechaHechoAntesDe() {
    Predicate<Hecho> filtro = Filtro.FECHA_HECHO_ANTES.crearFiltro("2023-12-01");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void fechaHechoDespuesDe() {
    Predicate<Hecho> filtro = Filtro.FECHA_HECHO_DESPUES.crearFiltro("2023-11-01");
    assertTrue(filtro.test(hecho));
  }
/*
  @Test
  void origenEs() {
    Predicate<Hecho> filtro = Filtro.ORIGEN_ES.crearFiltro("sistema");
    assertTrue(filtro.test(hecho));
  }

  @Test
  void origenNoEs() {
    Predicate<Hecho> filtro = Filtro.ORIGEN_NO_ES.crearFiltro("manual");
    assertTrue(filtro.test(hecho));
  }
  */
}