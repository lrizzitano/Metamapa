package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeCambio;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FuenteDinamicaTest {

  private final FuenteDinamica fuenteDinamica = FuenteDinamica.instance();
  private final SolicitudesFuenteDinamicaJPA solicitudesFuenteDinamica = mock(SolicitudesFuenteDinamicaJPA.class);
  private final HechosFuenteDinamicaJPA hechosFuenteDinamica = mock(HechosFuenteDinamicaJPA.class);
  private final Hecho unHecho = mock(Hecho.class);

  @BeforeEach
  public void setUp() {
    fuenteDinamica.setSolicitudesRepository(solicitudesFuenteDinamica);
    fuenteDinamica.setHechoRepository(hechosFuenteDinamica);
  }

  @Test
  void AgregarHechoModificaUsuarioQueCreoElHechoPasaASerContribuyente()
  {
    Usuario juanma = new Usuario("JUANMANUEL!","juanma","Brawstars", LocalDate.now(),"a");
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
