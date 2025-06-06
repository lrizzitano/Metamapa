package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa.LocalDateAdapter;
import ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa.PathAdapter;
import ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa.ServicioMetaMapa;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.mock;

@WireMockTest(httpsPort = 8085)
public class FuenteMetaMapaTest {

  ServicioMetaMapa fuente = new ServicioMetaMapa("http://localhost:8085/");

  @Nested
  class obtenerHechosTest {

    Set<Hecho> hechos = crearHechos();
    Gson gsonHechos = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();

    @Test
    public void obtenerHechoSinFiltros() {

      Filtro filtro = new NullFiltro();

      stubFor(get("/hechos")
          .withQueryParam(filtro.toString(), equalTo("Desastre natural"))
          .willReturn(aResponse().withBody(gsonHechos.toJson(hechos)).withStatus(200)));

      Assertions.assertEquals(hechos, fuente.obtenerHechos(filtro));
    }

    @Test
    public void obtenerHechoConFiltros() {

      Filtro filtro = new FiltroCategoria("Desastre natural");

      Set<Hecho> esperados = hechos.stream()
          .filter(h -> "Desastre natural".equals(h.categoria()))
          .collect(Collectors.toSet());

      stubFor(get("/hechos")
          .withQueryParam(filtro.toString(), equalTo("Desastre natural"))
          .willReturn(aResponse().withBody(gsonHechos.toJson(esperados)).withStatus(200)));

      Assertions.assertEquals(esperados, fuente.obtenerHechos(filtro));
    }

    @Test
    public void obtenerHechosLanzaAccesoARecursoFallido() {

      Filtro filtro = new FiltroCategoria("Desastre natural");

      stubFor(get(urlPathEqualTo("/hechos"))
          .withQueryParam(filtro.toString(), equalTo("Desastre natural"))
          .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER).withStatus(200)));

      Assertions.assertThrows(AccesoRecursoFallidoException.class, () -> {fuente.obtenerHechos(filtro);});
    }
  }

  @Nested
  class obtenerHechosDeColeccionTest {

    Coleccion coleccion = new Coleccion("Desastres Naturales Relevantes",
            "Incluye hechos relacionados con eventos naturales como incendios, inundaciones, etc.",
                        mock(Filtro.class),
                        mock(Fuente.class));
    Gson gsonColeccion = new GsonBuilder()
        .create();

    @Test
    public void obtenerHechosDeColeccionSinFiltros() {
      stubFor(get(urlMatching("/colecciones/[a-f0-9\\-]{36}/hechos")).
          willReturn(aResponse().withBody(gsonColeccion.toJson(coleccion)).withStatus(200)));

      Filtro filtro = new NullFiltro();

      Assertions.assertEquals(fuente.obtenerHechosDeColeccion(filtro, coleccion.getId()), coleccion.hechos(filtro));
    }

    @Test
    public void obtenerHechosDeColeccionConFiltro() {
      stubFor(get(urlMatching("/colecciones/[a-f0-9\\-]{36}/hechos")).
          willReturn(aResponse().withBody(gsonColeccion.toJson(coleccion)).withStatus(200)));

      Filtro filtro = new FiltroCategoria("Desastre natural");

      Assertions
          .assertEquals(fuente.obtenerHechosDeColeccion(filtro, coleccion.getId()),
              coleccion.hechos(filtro).stream().findFirst().orElse(null));
    }

    @Test
    public void obtenerHechosDeColeccionLanzaAccesoARecursoFallido() {
      stubFor(get(urlMatching("/colecciones/[a-f0-9\\-]{36}/hechos"))
          .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

      Filtro filtro = new FiltroCategoria("Desastre natural");

      Assertions.assertThrows(AccesoRecursoFallidoException.class,
          () -> {fuente.obtenerHechosDeColeccion(filtro, coleccion.getId());});
    }
  }

  @Nested
  class enviarSolicitudDeEliminacionTest {
    SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(mock(Hecho.class), mock(String.class));

    public void enviarSolicitudDeEliminacion() {
      fuente.enviarSolicitudDeEliminacion(solicitud);
      verify(postRequestedFor(urlEqualTo("/solicitudes")));
    }

    public void enviarSolicitudDeEliminacionLanzaAccesoARecursoFallido() {
      Assertions.assertThrows(AccesoRecursoFallidoException.class,
          () -> {fuente.enviarSolicitudDeEliminacion(solicitud);});
    }
  }

  public static Set<Hecho> crearHechos() {

    Hecho hecho1 = new Hecho(
        "Incendio en el bosque",
        "Un incendio afectó 200 hectáreas del bosque nacional.",
        "Desastre natural",
        -34.6037,
        -58.3816,
        LocalDate.of(2025, 6, 5),
        LocalDate.of(2025, 6, 4),
        mock(Origen.class),
        mock(Path.class),
        mock(Usuario.class)
    );

    Hecho hecho2 = new Hecho(
        "Accidente vial en avenida principal",
        "Choque múltiple con varios vehículos involucrados.",
        "Accidente",
        -34.6090,
        -58.3840,
        LocalDate.of(2025, 6, 5),
        LocalDate.of(2025, 6, 4),
        mock(Origen.class),
        mock(Path.class),
        mock(Usuario.class)
    );

    Set<Hecho> hechos = new HashSet<>();
    hechos.add(hecho1);
    hechos.add(hecho2);

    return hechos;
  }
}