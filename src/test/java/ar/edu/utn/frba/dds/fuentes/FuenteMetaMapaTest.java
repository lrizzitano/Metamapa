package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa.ServicioMetaMapa;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    @Test
    public void obtenerHecho(WireMockRuntimeInfo wmRuntimeInfo) {

      Filtro filtro = new FiltroCategoria("Desastre natural");
      fuente.obtenerHechos(filtro);
      verify(getRequestedFor(urlEqualTo(fuente.getUrlAPI())));
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
        mock(Filtro.class),
        mock(Fuente.class));
    Gson gsonColeccion = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    @Test
    public void obtenerHechosDeColeccion() {

      Filtro filtro = new NullFiltro();
      fuente.obtenerHechosDeColeccion(filtro, coleccion.getId());

      verify(getRequestedFor(urlEqualTo(fuente.getUrlAPI())));
    }

    @Test
    public void obtenerHechosDeColeccionLanzaAccesoARecursoFallido() {
      stubFor(get(urlEqualTo("/colecciones/:[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}/hechos"))
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
      verify(getRequestedFor(urlEqualTo(fuente.getUrlAPI())));
    }

    public void enviarSolicitudDeEliminacionLanzaAccesoARecursoFallido() {
      Assertions.assertThrows(AccesoRecursoFallidoException.class,
          () -> {fuente.enviarSolicitudDeEliminacion(solicitud);});
    }
  }
}