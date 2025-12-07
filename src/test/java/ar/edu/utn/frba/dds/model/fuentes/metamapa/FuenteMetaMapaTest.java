package ar.edu.utn.frba.dds.model.fuentes.metamapa;

import ar.edu.utn.frba.dds.model.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest
public class FuenteMetaMapaTest {

  static Gson gson;
  static FuenteMetaMapa fuente;

  @BeforeAll
  public static void setup(WireMockRuntimeInfo wmRuntimeInfo) {
    fuente = new FuenteMetaMapa(wmRuntimeInfo.getHttpBaseUrl());
    gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .setPrettyPrinting()
        .create();
  }

  @Test
  public void respuestaExitosaConBodyVacioRetornaSetVacio() {

    stubFor(get(urlPathEqualTo("/api/hechos"))
        .willReturn(aResponse()
            .withStatus(204)));

    Assertions.assertTrue(fuente.obtenerHechos(new NullFiltro()).isEmpty());
  }

  @Test
  public void respuestaConErrorLanzaExcepcion() {

    stubFor(get(urlPathEqualTo("/api/hechos"))
        .willReturn(aResponse()
            .withStatus(404)));

    Assertions.assertThrows(AccesoRecursoFallidoException.class, ()
        -> fuente.obtenerHechos(new NullFiltro()));
  }

  @Test
  public void respuestaExitosaRetornaHechos() {
    Set<Hecho> hechosEsperados = new HashSet<>();
    hechosEsperados.add(new Hecho(null,
        "Inundación en zona norte",
        "Fuertes lluvias causaron una inundación severa",
        "Desastre natural",
        new Ubicacion(-34.6037,
        -58.3816, null, null),
        LocalDateTime.of(2025, 5, 10,11,12),
        LocalDateTime.of(2025, 5, 9,11,12),
        Origen.CONTRIBUYENTE
    ));

    stubFor(get(urlPathEqualTo("/api/hechos"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(gson.toJson(hechosEsperados))));

    assertThat(hechosEsperados)
        .usingRecursiveComparison()
        .ignoringCollectionOrder()
        .isEqualTo(fuente.obtenerHechos(new NullFiltro()));
  }

  @Test
  public void seEnviaElFiltro() {
    Filtro filtro = new FiltroCategoria("hola");

    stubFor(get(urlPathEqualTo("/api/hechos"))
        .willReturn(aResponse()
            .withStatus(204)));
    fuente.obtenerHechos(filtro);
    verify(getRequestedFor(urlPathEqualTo("/api/hechos"))
        .withQueryParam("categoria", equalTo("hola")));
  }

  @Test
  public void seEnviaCorrectamenteIdColeccion() {
    String idEsperado = "hola";

    stubFor(get(urlPathMatching("/api/colecciones/[^/]+/hechos"))
        .willReturn(aResponse().withStatus(204)));

    fuente.obtenerHechosDeColeccion(new NullFiltro(), idEsperado);

    verify(getRequestedFor(urlPathEqualTo("/api/colecciones/" + idEsperado + "/hechos")));
  }



}