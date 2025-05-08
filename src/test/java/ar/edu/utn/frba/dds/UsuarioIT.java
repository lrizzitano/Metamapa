package ar.edu.utn.frba.dds;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Random;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//En este archivo se testea la funcionalidad completa de COLECCION, FUENTE ESTATICA, USUARIO y FORMATEADORCSV

public class UsuarioIT {

  private final Usuario unUsuario = new Usuario("Pablo", "Gabarini",21);
  private final Administrador unAdministrador = new Administrador("PabloCrack", "GabariniCrack",88);

  private final FuenteEstatica unaFuente = new FuenteEstatica("fires-all-formateado.csv");
  private final Coleccion unaColeccion = new Coleccion("Incendios forestales",
      "Una Descripcion", Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);


  @Test
  void UnaColeccion_CreadaPorAdmin_TraeTodosSusHechos_DeSuFuente() {
    unAdministrador.crearColeccion("Incendios forestales","Una Descripcion",
                                    Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);

    Assertions.assertEquals(230141, unUsuario.verHechos(unaColeccion).size());
  }

  @Test
  void LaColeccion_CreadaPorAdmin_LeTraeAlUsuario_SusHechosFiltrados_DeSuFuente() {
    final Predicate<Hecho>  unFiltro = Filtro.FECHA_HECHO_DESPUES.crearFiltro("2021-01-01");
    unAdministrador.crearColeccion("Incencios forestales","Una Descripcion",
        Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);

    Assertions.assertTrue(unUsuario.verHechosFiltrados(unaColeccion,unFiltro).size() < 10000);
  }

  @Test
  void UsuarioCargaSolicitud_AdminAcepta_HechoSeElimina() {
    unAdministrador.crearColeccion("Incendios forestales","Una Descripcion",
        Filtro.CATEGORIA.crearFiltro("Incendio forestal") ,unaFuente);
    Hecho unHecho = unUsuario.verHechos(unaColeccion).stream().toList().get(0);
    Solicitud miSolicitud = unUsuario.crearSolicitud(unHecho,"La informacion es falsa");
    unAdministrador.aceptarSolicitud(miSolicitud);
    assertFalse(unUsuario.verHechos(unaColeccion).contains(unHecho));
  }
}