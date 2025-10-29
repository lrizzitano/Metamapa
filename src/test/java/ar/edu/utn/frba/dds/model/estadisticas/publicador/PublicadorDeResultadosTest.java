package ar.edu.utn.frba.dds.model.estadisticas.publicador;

import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.objetosDeEstudio.EstudioDeColeccion;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultados;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultadosCSV;
import ar.edu.utn.frba.dds.model.estadisticas.publicadorDeResultados.PublicadorDeResultadosJSON;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstadistico;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudDeEliminacionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PublicadorDeResultadosTest {
  private RepoColecciones coleccionesRepository;
  private EstudioDeCategoria estudioCategoria;
  private EstudioDeColeccion estudioColeccion;
  private Set<Coleccion> colecciones;
  private Set<Hecho> hechos;
  private final Fuente fuente = mock(Fuente.class);
  private final Fuente fuente2 = mock(Fuente.class);

  @BeforeEach
  public void setup() {
    coleccionesRepository = mock(RepoColecciones.class);
    hechos = crearListaHechos();
    colecciones = crearColecciones();
    estudioCategoria = new EstudioDeCategoria(coleccionesRepository);
    estudioColeccion = new EstudioDeColeccion(coleccionesRepository);
  }

  @Test
  public void exportaEstudiosCorrectamente() {
    LocalDateTime desde = LocalDateTime.of(2023,1,1,22,54);

    when(coleccionesRepository.findAll()).thenReturn(colecciones);
    when(fuente.obtenerHechos(any())).thenReturn(hechos);
    when(fuente2.obtenerHechos(any())).thenReturn(hechos);

    List<ResultadoEstadistico> estadisticasCategoria = estudioCategoria.estudiar(desde);
    List<ResultadoEstadistico> estadisticasColeccion = estudioColeccion.estudiar(desde);

    PublicadorDeResultados publiCategoriaCSV = new PublicadorDeResultadosCSV("categorias.csv");
    PublicadorDeResultados publiCategoriaJSON = new PublicadorDeResultadosJSON("categorias.json");

    PublicadorDeResultados publiColeccionCSV = new PublicadorDeResultadosCSV("coleccion.csv");
    PublicadorDeResultados publiColeccionJSON = new PublicadorDeResultadosJSON("coleccion.json");

    publiCategoriaCSV.comunicarResultados(estadisticasCategoria);
    publiCategoriaJSON.comunicarResultados(estadisticasCategoria);
    publiColeccionCSV.comunicarResultados(estadisticasColeccion);
    publiColeccionJSON.comunicarResultados(estadisticasColeccion);

    // se podria assertear que existen, pero se verifica manualmente que tengan lo que queremos
  }

  private Set<Coleccion> crearColecciones() {
    Coleccion coleccion1 = new Coleccion(
        "Eventos sociales y climáticos",
        "Hechos relacionados con protestas y desastres naturales",
        new NullFiltro(),
        fuente,
        new ConsensoNull(),
        mock(SolicitudDeEliminacionRepository.class)
    );
    Coleccion coleccion2 = new Coleccion(
        "futbol",
        "boca",
        new NullFiltro(),
        fuente2,
        new ConsensoNull(),
        mock(SolicitudDeEliminacionRepository.class)
    );
    return Set.of(coleccion1,coleccion2);
  }

  // 3 hechos en la pampa, 1 en burzaco
  private static Set<Hecho> crearListaHechos() {

    Ubicacion laPampa = new Ubicacion(12.2,12.2, Provincia.LA_PAMPA,null);
    Ubicacion burzaco = new Ubicacion(12.2, 12.2, Provincia.BUENOS_AIRES,null);

    Hecho hecho1 = new Hecho(
        null,
        "Muerte en La Pampa",
        "Fuertes lluvias provocaron inundaciones en barrios céntricos",
        "Desastre Natural",
        laPampa,
        LocalDateTime.now(),
        LocalDateTime.of(2024, 3, 15, 22, 14),
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
        "Fuego en La Pampa",
        "el derretimiento de los polos inundo la plata",
        "Desastre Natural",
        laPampa,
        LocalDateTime.now(),
        LocalDateTime.of(2024, 3, 15, 14, 30),
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

    return Set.of(hecho1, hecho2, hecho3, hecho4, hecho5);
  }
}