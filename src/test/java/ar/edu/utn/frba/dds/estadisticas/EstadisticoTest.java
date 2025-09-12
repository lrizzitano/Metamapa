package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Provincia;
import ar.edu.utn.frba.dds.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudDeEliminacionRepository;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.solicitudes.deteccionSpam.NullDetector;
import ar.edu.utn.frba.dds.usuarios.Administrador;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


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


    hpc1 = new HechosPorProvincia(Provincia.LA_PAMPA,(long)3);
     hpc2 = new HechosPorProvincia(Provincia.LA_RIOJA,(long) 2);

    hechosXprovincia = List.of(hpc1, hpc2);
    consensoNull = new ConsensoNull();
     filtroNulo = new NullFiltro();
  }

  @Test
  public void provinciaConMasHechosDeUnaColleccion() {

    estadistico = new Estadistico();

    repoFuentes.agregarFuente(fuenteDinamica);


    coleccionAux = new Coleccion("prueb","Una descripcion",filtroNulo,
        fuenteDinamica,consensoNull,new SolicitudesDeEliminacionJPA());
    repoColecciones.save(coleccionAux);

    fecha = LocalDateTime.now();

    estudioColeccion =
        new ResultadoEstudioColeccion(fecha,coleccionAux,(long)5,hechosXprovincia);

    entityManager().persist(estudioColeccion);

    String provincia = estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccionAux,fecha);

    Assertions.assertEquals("LA_PAMPA",provincia);
    System.out.println(provincia);
  }

  @Test
  public void categoriaConMasHechosReportados() {

    fecha = LocalDateTime.now();

    estudioCategoria =
        new ResultadoEstudioCategoria(fecha,"Incendio",5, 12 ,hechosXprovincia);

    estadistico = new Estadistico();

    entityManager().persist(estudioCategoria);

    String categoria = estadistico.categoriaConMasHechosReportados(fecha);

    System.out.println(categoria);

    Assertions.assertEquals("Incendio",categoria);

  }

  @Test
  public void horaPicoDeReporteDeUnaCategoria(){

    fecha = LocalDateTime.now();

    estudioCategoria =
        new ResultadoEstudioCategoria(fecha,"Incendio",5, 12 ,hechosXprovincia);

    estadistico = new Estadistico();

    entityManager().persist(estudioCategoria);

    LocalTime horaPico = estadistico.horaPicoDeReporteDeUnaCategoria("Incendio",fecha);

    Assertions.assertEquals("00:12",horaPico.toString());
  }

  @Test
  public void provinciaConMasHechosReportadosDeUnaCategoria(){

    fecha = LocalDateTime.now();

    estudioCategoria =
        new ResultadoEstudioCategoria(fecha,"Incendio",5, 12 ,hechosXprovincia);

    estadistico = new Estadistico();

    entityManager().persist(estudioCategoria);

    String provincia = estadistico.provinciaConMasHechosReportadosDeUnaCategoria("Incendio",fecha);

    Assertions.assertEquals("LA_PAMPA",provincia);

  }

}
