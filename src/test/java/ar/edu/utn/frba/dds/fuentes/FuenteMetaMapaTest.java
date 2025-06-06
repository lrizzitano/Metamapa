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
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacionRepository;
import ar.edu.utn.frba.dds.usuarios.Usuario;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.mock;

@WireMockTest // puerto aleatorio
public class FuenteMetaMapaTest {

  static ServicioMetaMapa fuente;

  @BeforeAll
  public static void setup(WireMockRuntimeInfo wmRuntimeInfo) {
    fuente = new ServicioMetaMapa(wmRuntimeInfo.getHttpBaseUrl());
  }

  @Nested
  class obtenerHechosTest {

    Set<Hecho> hechos = crearHechos();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();

    @Test
    public void obtenerHecho(WireMockRuntimeInfo wmRuntimeInfo) {

      Filtro filtro = new FiltroCategoria("Desastre natural");

      Set<Hecho> esperado = hechos.stream()
          .filter(h -> "Desastre natural".equals(h.categoria()))
          .collect(Collectors.toSet());

      stubFor(get(urlPathEqualTo("/hechos"))
          .withQueryParam(filtro.toString(), equalTo("Desastre natural"))
          .willReturn(aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/json")
              .withBody(gson.toJson(esperado))));

      Assertions.assertEquals(esperado, fuente.obtenerHechos(filtro));
    }

    @Test
    public void obtenerHechosLanzaAccesoARecursoFallido() {
      stubFor(get(urlEqualTo("/hechos"))
          .willReturn(aResponse()
              .withFault(Fault.CONNECTION_RESET_BY_PEER)));

      Filtro filtro = new FiltroCategoria("Desastre natural");

      Assertions.assertThrows(AccesoRecursoFallidoException.class, () -> {fuente.obtenerHechos(filtro);});
    }
  }

  @Nested
  class obtenerHechosDeColeccionTest {

    Coleccion coleccion = new Coleccion(
        "Desastres Naturales Relevantes",
        "Incluye hechos relacionados con eventos naturales como incendios, inundaciones, etc.",
        new FiltroCategoria("Desastre Naturales"),
        mock(Fuente.class),
        mock(SolicitudDeEliminacionRepository.class));
    Set<Hecho> hechos = crearHechos();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();

    @Test
    public void obtenerHechosDeColeccion() {

      Filtro filtro = new FiltroCategoria("Desastre Naturales");

      Set<Hecho> esperado = hechos.stream()
          .filter(h -> "Desastre natural".equals(h.categoria()))
          .collect(Collectors.toSet());

      stubFor(get(urlEqualTo("/Colecciones/:"+ coleccion.getId()) + "/hechos")
          .withQueryParam(filtro.toString(), equalTo("Desastre natural"))
          .willReturn(aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/json")
              .withBody(gson.toJson(esperado))));

      Assertions.assertEquals(esperado,fuente.obtenerHechosDeColeccion(filtro, coleccion.getId()));
    }

    @Test
    public void obtenerHechosDeColeccionLanzaAccesoARecursoFallido() {
      stubFor(get(urlEqualTo("/colecciones/:" + coleccion.getId()) + "/hechos")
          .willReturn(aResponse()
              .withFault(Fault.CONNECTION_RESET_BY_PEER)));

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
      verify(getRequestedFor(urlEqualTo("/solicitudes")));
    }

    public void enviarSolicitudDeEliminacionLanzaAccesoARecursoFallido() {
      Assertions.assertThrows(AccesoRecursoFallidoException.class,
          () -> {fuente.enviarSolicitudDeEliminacion(solicitud);});
    }
  }

  public static Set<Hecho> crearHechos() {

    Set<Hecho> hechos = new HashSet<>();

    hechos.add(new Hecho(
        "Terremoto en la región norte",
        "Se registró un terremoto de magnitud 7.2",
        "Desastre natural",
        -33.456,
        -70.648,
        LocalDate.now(),
        LocalDate.of(2025, 5, 15),
        mock(Origen.class),
        mock(Path.class),
        mock(Usuario.class)
    ));

    hechos.add(new Hecho(
        "Inauguración biblioteca central",
        "Se inauguró la nueva biblioteca en la ciudad",
        "Evento cultural",
        -33.456,
        -70.648,
        LocalDate.now(),
        LocalDate.of(2025, 6, 1),
        mock(Origen.class),
        mock(Path.class),
        mock(Usuario.class)
    ));

    return hechos;
  }
}