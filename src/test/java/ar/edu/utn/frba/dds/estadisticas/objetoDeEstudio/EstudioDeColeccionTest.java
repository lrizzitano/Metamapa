package ar.edu.utn.frba.dds.estadisticas.objetoDeEstudio;

import ar.edu.utn.frba.dds.estadisticas.Provincia;
import ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio.EstudioDeColeccion;
import ar.edu.utn.frba.dds.estadisticas.objetosDeEstudio.ObjetoDeEstudio;
import ar.edu.utn.frba.dds.execpciones.NoExisteInformacionException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.hechos.Origen;
import ar.edu.utn.frba.dds.hechos.Ubicacion;
import ar.edu.utn.frba.dds.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudDeEliminacionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EstudioDeColeccionTest {

  private ColeccionesRepository coleccionesRepository;
  private EstudioDeColeccion estudioColecciones;
  private List<Coleccion> colecciones;
  private List<Hecho> hechos;

  @BeforeEach
  public void setup() {
    coleccionesRepository = mock(ColeccionesRepository.class);
    hechos = crearListaHechos();
    colecciones = crearColecciones();
    estudioColecciones = new EstudioDeColeccion(coleccionesRepository);
  }

  @Test
  public void recuperaLosHechosDesdeUnaFechaDeSuRepositorio() {
    when(coleccionesRepository.findAll()).thenReturn(colecciones);
    Assertions.assertEquals(colecciones.size(), estudioColecciones.recolectarDatos().size());
  }

  @Test
  public void recuperaLosHechosDesdeUnaFechaDeSuRepositorio_lanzaExcepcion() {
    when(coleccionesRepository.findAll()).thenReturn(null);
    Assertions.assertThrows(NoExisteInformacionException.class , () -> estudioColecciones.recolectarDatos());
  }

  @Test
  public void determinaLaProvinciaConMasHechosDeUnaColeccion() {
    LocalDate desde = LocalDate.of(2023,1,1);

  }

  private List<Coleccion> crearColecciones() {
    Coleccion coleccion1 = new Coleccion(
        "Eventos sociales y climáticos",
        "Hechos relacionados con protestas y desastres naturales",
        mock(Filtro.class),
        mock(Fuente.class),
        mock(Consenso.class),
        mock(SolicitudDeEliminacionRepository.class)
    );
    Coleccion coleccion2 = new Coleccion(
        "futbol",
        "boca",
        mock(Filtro.class),
        mock(Fuente.class),
        mock(Consenso.class),
        mock(SolicitudDeEliminacionRepository.class)
    );
    return List.of(coleccion1,coleccion2);
  }

  // 3 hechos en la pampa, 1 en burzaco
  private static List<Hecho> crearListaHechos() {

    Ubicacion laPampa = new Ubicacion(null,null, Provincia.LA_PAMPA);
    Ubicacion burzaco = new Ubicacion(null, null, Provincia.PROV_BUENOS_AIRES);

    Hecho hecho1 = new Hecho(
        null,
        "Inundación en La Pampa",
        "Fuertes lluvias provocaron inundaciones en barrios céntricos",
        "Desastre Natural",
        laPampa,
        LocalDateTime.now(),
        LocalDateTime.of(2024, 3, 15, 23, 59),
        Origen.CONTRIBUYENTE
    );

    Hecho hecho2 = new Hecho(
        null,
        "Inundación en La Pampa",
        "Fuertes lluvias provocaron inundaciones en barrios perifericos",
        "Desastre Natural",
        laPampa,
        LocalDateTime.now(),
        LocalDateTime.of(2024, 3, 15, 23, 59),
        Origen.CONTRIBUYENTE
    );

    Hecho hecho3 = new Hecho(
        null,
        "Inundación en La Pampa",
        "el derretimiento de los polos inundo la plata",
        "Desastre Natural",
        laPampa,
        LocalDateTime.now(),
        LocalDateTime.of(2024, 3, 15, 23, 59),
        Origen.CONTRIBUYENTE
    );

    Hecho hecho4 = new Hecho(
        null,
        "tornado en burzaco",
        "pobre cpu 10",
        "Desastre Natural",
        burzaco,
        LocalDateTime.now(),
        LocalDateTime.of(2024, 3, 15, 23, 59),
        Origen.CONTRIBUYENTE
    );

    Hecho hecho5 = new Hecho(
        null,
        "cpu 10 salva una familia en un edificio en llamas",
        "grande cpu 10",
        "Milagro",
        burzaco,
        LocalDateTime.now(),
        LocalDateTime.of(2023, 3, 15, 23, 59),
        Origen.CONTRIBUYENTE
    );

    return List.of(hecho1, hecho2, hecho3, hecho4);
  }
}
