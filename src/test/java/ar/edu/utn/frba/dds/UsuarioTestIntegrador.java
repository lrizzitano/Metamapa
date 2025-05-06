package ar.edu.utn.frba.dds;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//En este archivo se testea la funcionalidad completa de COLECCION, FUENTE ESTATICA, USUARIO y FORMATEADORCSV

public class UsuarioTestIntegrador {

  private final Usuario unUsuario = new Usuario("Pablo", "Gabarini",21);
  private final Administrador unAdministrador = new Administrador("PabloCrack", "GabariniCrack",88);

  private final Hecho unHecho = mock(Hecho.class);
  private final Solicitudes solicitudes = Solicitudes.instance();

  private final FuenteEstatica unaFuente = new FuenteEstatica("fires-all-formateado.csv");
  private final Coleccion unaColeccion = new Coleccion("Incencios forestales","Una Descripcion"
                                                        , Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);

  private final Predicate<Hecho>  filtro = Filtro.FECHA_HECHO_DESPUES.crearFiltro("2021-01-01");

  @Test
  void UnaColeccion_CreadaPorAdmin_TraeTodosSusHechos_DeSuFuente() {

    unAdministrador.crearColeccion("Incencios forestales","Una Descripcion",
                                    Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);

    Assertions.assertTrue(unUsuario.verHechos(unaColeccion).size() > 10000);
  }

  @Test
  void LaColeccion_CreadaPorAdmin_LeTraeAlUsuario_SusHechosFiltrados_DeSuFuente() {

    unAdministrador.crearColeccion("Incencios forestales","Una Descripcion",
        Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);

    Assertions.assertTrue(unUsuario.verHechosFiltrados(unaColeccion,filtro).size() < 10000);
  }

  @Test
  void Usuario_CargaSolicitud_SeLlenaLaLista() {

    unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertTrue(solicitudes.getPendientes().size() > 1);
  }

  @Test
  void Usuario_CargaSolicitud_LaCargaEsCorrectaDelFundamento() {

    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertEquals("La informacion es falsa",miSolicitud.getFundamento());
  }

  @Test
  void Usuario_CargaSolicitud_LaCargaEsCorrectaDelHecho() {

    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertEquals(unHecho,miSolicitud.getHecho());
  }

  @Test
  void Usuario_CargaSolicitud_LaCargaEsCorrectaDeLaSolicitud() {

    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    Assertions.assertTrue(solicitudes.getPendientes().contains(miSolicitud));
  }
}