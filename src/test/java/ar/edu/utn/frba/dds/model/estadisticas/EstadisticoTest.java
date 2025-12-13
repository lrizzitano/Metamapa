package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.model.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.RechazosDeEliminacion;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class EstadisticoTest implements SimplePersistenceTest {

  FuenteDinamica fuenteDinamica = FuenteDinamica.instance();
  RepoColecciones repoColecciones = new RepoColecciones();
  FuentesRepositoryJPA repoFuentes = new FuentesRepositoryJPA();

  HechosPorProvincia hpc1;
  HechosPorProvincia hpc2;

  List<HechosPorProvincia> hechosXprovincia;
  Consenso consensoNull;
  Filtro filtroNulo;

  Coleccion coleccionAux;

  LocalDateTime fecha;

  ResultadoEstudioColeccion estudioColeccion;
  Estadistico estadistico;

  ResultadoEstudioCategoria estudioCategoria;

  @BeforeEach
  void setUp() {

    fecha = LocalDateTime.now();

    estadistico = new Estadistico();

    hpc1 = new HechosPorProvincia(Provincia.LA_PAMPA, (long) 3);
    hpc2 = new HechosPorProvincia(Provincia.LA_RIOJA, (long) 2);

    hechosXprovincia = List.of(hpc1, hpc2);
    consensoNull = new ConsensoNull();
    filtroNulo = new NullFiltro();

    estudioCategoria =
        new ResultadoEstudioCategoria(fecha, "Incendio", 5, 12, hechosXprovincia);

    coleccionAux = new Coleccion("prueb", "Una descripcion", filtroNulo,
        fuenteDinamica, consensoNull, new SolicitudesDeEliminacionJPA());

    estudioColeccion =
        new ResultadoEstudioColeccion(fecha, coleccionAux, (long) 5, hechosXprovincia);
  }

  @Test
  public void provinciaConMasHechosDeUnaColleccion() {

    repoFuentes.agregarFuente(fuenteDinamica);

    repoColecciones.save(coleccionAux);

    entityManager().persist(estudioColeccion);
    entityManager().flush();

    Provincia provincia = estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccionAux, fecha,fecha);

    Assertions.assertEquals(Provincia.LA_PAMPA, provincia);
    System.out.println(provincia);
  }

  @Test
  public void categoriaConMasHechosReportados() {

    entityManager().persist(estudioCategoria);
    entityManager().flush();

    String categoria = estadistico.categoriaConMasHechosReportados(fecha,fecha);

    System.out.println(categoria);

    Assertions.assertEquals("Incendio", categoria);

  }

  @Test
  public void horaPicoDeReporteDeUnaCategoria() {

    entityManager().persist(estudioCategoria);
    entityManager().flush();

    LocalTime horaPico = estadistico.horaPicoDeReporteDeUnaCategoria("Incendio", fecha);

    Assertions.assertEquals("00:12", horaPico.toString());
  }

  @Test
  public void provinciaConMasHechosReportadosDeUnaCategoria() {

    entityManager().persist(estudioCategoria);
    entityManager().flush();

    Provincia provincia = estadistico.provinciaConMasHechosReportadosDeUnaCategoria("Incendio", fecha, fecha);

    Assertions.assertEquals(Provincia.LA_PAMPA, provincia);

  }

  @Test
  public void cantidadSpam() {
    entityManager().persist(new RechazosDeEliminacion("re interesante", 2, 1));
    entityManager().persist(new RechazosDeEliminacion("aún más interesante", 5, 2));
    entityManager().flush();

    Assertions.assertEquals(3, estadistico.cantidadDeSpamEnSolicitudesDeEliminacion());
  }

}
