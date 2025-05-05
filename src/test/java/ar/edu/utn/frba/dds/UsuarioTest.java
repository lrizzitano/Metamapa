package ar.edu.utn.frba.dds;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class UsuarioTest {

  private final Usuario unUsuario = new Usuario("Pablo", "Gabarini",21);
  private final Hecho unHecho = mock(Hecho.class);
  private final Solicitudes solicitudes = Solicitudes.instance();

  private final FuenteEstatica unaFuente = new FuenteEstatica("fires-all-formateado.csv");
  private final Coleccion unaColeccion = new Coleccion("Incencios forestales","Una Descripcion"
                                                        , Filtro.CATEOGIRA.crearFiltro("Incendio") ,unaFuente);
  private final Predicate<Hecho> filtroFechaCarga = Filtro.CATEOGIRA.crearFiltro("2023-01-01");
  @Test
  void SeVenTodosLosHechosDeLaColeccion() {

    Assertions.assertTrue(unUsuario.verHechos(unaColeccion).size() > 10000);
  }

  @Test
  void SeVenHechosLuegoDeAplicadoElFiltro() {

    Assertions.assertTrue(unUsuario.verHechosFiltrados(unaColeccion,filtroFechaCarga).size() < 10000);
  }

  @Test
  void cargaSolicitud_SeLlenaLaLista() {

    unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertTrue(solicitudes.instance().getPendientes().size() > 1);
  }

  @Test
  void cargaSolicitud_LaCargaEsCorrectaDelFundamento() {

    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertEquals("La informacion es falsa",miSolicitud.getFundamento());
  }

  @Test
  void cargaSolicitud_LaCargaEsCorrectaDelHecho() {

    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertEquals(unHecho,miSolicitud.getHecho());
  }

  @Test
  void cargaSolicitud_LaCargaEsCorrectaDeLaSolicitud() {

    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertTrue(solicitudes.instance().getPendientes().contains(miSolicitud));
  }
}