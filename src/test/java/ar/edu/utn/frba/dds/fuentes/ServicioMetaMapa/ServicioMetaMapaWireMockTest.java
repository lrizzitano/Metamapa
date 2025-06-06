package ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa;

import ar.edu.utn.frba.dds.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@WireMockTest
public class ServicioMetaMapaWireMockTest {
  static Gson gson;
  static ServicioMetaMapa fuente;

  @BeforeAll
  public static void setup(WireMockRuntimeInfo wmRuntimeInfo) {
    fuente = new ServicioMetaMapa(wmRuntimeInfo.getHttpBaseUrl());
    gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();
  }

  @Test
  public void respuestaExitosaConBodyVacioLanzaExcepcion() {
    Filtro filtro = new FiltroCategoria("Cualquier categoría");

    stubFor(get(urlPathEqualTo("/hechos"))
        .withQueryParam(filtro.toString(), equalTo("Cualquier categoría"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(""))); // Body vacío

    Assertions.assertThrows(AccesoRecursoFallidoException.class, () -> {
      fuente.obtenerHechos(filtro);
    });
  }

  @Test
  public void respuesta204SinContenidoLanzaExcepcion() {
    Filtro filtro = new FiltroCategoria("Categoría vacía");

    stubFor(get(urlPathEqualTo("/hechos"))
        .withQueryParam(filtro.toString(), equalTo("Categoría vacía"))
        .willReturn(aResponse()
            .withStatus(204))); // No Content

    Assertions.assertThrows(AccesoRecursoFallidoException.class, () -> {
      fuente.obtenerHechos(filtro);
    });
  }

  @Test
  public void obtenerHechos_respuestaExitosa() {
    Filtro filtro = new FiltroCategoria("Desastre natural");

    Set<Hecho> hechosEsperados = new HashSet<>();
    hechosEsperados.add(new Hecho(
        "Inundación en zona norte",
        "Fuertes lluvias causaron una inundación severa",
        "Desastre natural",
        -34.6037,
        -58.3816,
        LocalDate.of(2025, 5, 10),
        LocalDate.of(2025, 5, 9),
        Origen.CONTRIBUYENTE
    ));

    stubFor(get(urlPathEqualTo("/hechos"))
        .withQueryParam(filtro.toString(), equalTo("Desastre natural"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(gson.toJson(hechosEsperados))));

    Set<Hecho> resultado = fuente.obtenerHechos(filtro);

    Assertions.assertEquals(hechosEsperados, resultado);
  }
}