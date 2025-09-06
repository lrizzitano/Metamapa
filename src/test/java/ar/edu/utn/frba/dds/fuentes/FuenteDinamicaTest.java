package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaMemoria;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesFuenteDinamicaMemoria;
import ar.edu.utn.frba.dds.usuarios.Usuario;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuenteDinamicaTest {

  private final FuenteDinamica fuenteDinamica = FuenteDinamica.instance();
  private final SolicitudesFuenteDinamicaMemoria solicitudesFuenteDinamica = mock(SolicitudesFuenteDinamicaMemoria.class);
  private final HechosFuenteDinamicaMemoria hechosFuenteDinamica = mock(HechosFuenteDinamicaMemoria.class);
  private final Hecho unHecho = mock(Hecho.class);

  @BeforeEach
  public void setUp() {
    fuenteDinamica.setSolicitudesRepository(solicitudesFuenteDinamica);
    fuenteDinamica.setHechoRepository(hechosFuenteDinamica);
  }

  @Test
  void AgregarHechoModificaUsuarioQueCreoElHechoPasaASerContribuyente()
  {
    Usuario juanma = new Usuario("JUANMANUEL!","Brawstars",12);
    when(unHecho.contribuyente()).thenReturn(juanma);

    fuenteDinamica.agregarHecho(unHecho);

    Assertions.assertTrue(juanma.esContribuyente());
  }

  @Test
  void AceptarSolicitudDeCambioLlamaASusDosRepositoriosInyectados()
  {
    SolicitudDeCambio unaSolicitudDeCambio = mock(SolicitudDeCambio.class);
    when(unaSolicitudDeCambio.getHechoParacambiar()).thenReturn(unHecho);
    when(unaSolicitudDeCambio.getHechoModificado()).thenReturn(unHecho);

    fuenteDinamica.aceptarSolicitudDeCambio(unaSolicitudDeCambio);

    verify(hechosFuenteDinamica).actualizar(unHecho, unHecho);
    verify(solicitudesFuenteDinamica).aceptarSolicitud(unaSolicitudDeCambio);

  }
}
