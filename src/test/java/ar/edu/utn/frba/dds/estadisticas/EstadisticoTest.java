package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.HechosPorProvincia;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class EstadisticoTest implements SimplePersistenceTest {


  RepoColecciones repoColecciones = new RepoColecciones();
  FuentesRepositoryJPA repoFuentes = new FuentesRepositoryJPA();
  FuenteDinamica fuenteDinamica = FuenteDinamica.instance();

  HechosPorProvincia hpc1 = new HechosPorProvincia(Provincia.LA_PAMPA,(long)3);
  HechosPorProvincia hpc2 = new HechosPorProvincia(Provincia.LA_RIOJA,(long) 2);

  List<HechosPorProvincia> hechosXColecciones = List.of(hpc1, hpc2);

  Coleccion coleccionAux = new Coleccion("prueb","Una descripcion",new NullFiltro(),
      fuenteDinamica,new ConsensoNull(),new SolicitudesDeEliminacionJPA());

  LocalDateTime fecha = LocalDateTime.now();

  ResultadoEstudioColeccion estudioColeccion =
      new ResultadoEstudioColeccion(fecha,coleccionAux,(long)5,hechosXColecciones);
  Estadistico estadistico = new Estadistico();

  @BeforeEach
  void setUp() {
    repoFuentes.agregarFuente(fuenteDinamica);
    repoColecciones.save(coleccionAux);
  }

  @Test
  public void test() {
    entityManager().persist(estudioColeccion);

    String provincia = estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccionAux,fecha);

    Assertions.assertEquals("LA_PAMPA",provincia);
    System.out.println(provincia);
  }


}
