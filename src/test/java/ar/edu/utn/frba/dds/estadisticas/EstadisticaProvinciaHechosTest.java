package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.HechosPorProvincia;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioCategoria;
import ar.edu.utn.frba.dds.estadisticas.resultadoEstadistico.ResultadoEstudioColeccion;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.hechos.Coleccion;
import ar.edu.utn.frba.dds.hechos.Provincia;
import ar.edu.utn.frba.dds.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

public class EstadisticaProvinciaHechosTest implements SimplePersistenceTest {


  RepoColecciones repoColecciones;
  FuentesRepositoryJPA repoFuentes;
  FuenteDinamica fuenteDinamica;

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

    repoColecciones = new RepoColecciones();
    repoFuentes = new FuentesRepositoryJPA();
    fuenteDinamica = FuenteDinamica.instance();

    hpc1 = new HechosPorProvincia(Provincia.LA_PAMPA,(long)3);
    hpc2 = new HechosPorProvincia(Provincia.LA_RIOJA,(long) 2);

    hechosXprovincia = List.of(hpc1, hpc2);
    consensoNull = new ConsensoNull();
    filtroNulo = new NullFiltro();

    coleccionAux = new Coleccion("prueb","Una descripcion",filtroNulo,
        fuenteDinamica,consensoNull,new SolicitudesDeEliminacionJPA());
    fecha = LocalDateTime.now();
    estudioColeccion =
        new ResultadoEstudioColeccion(fecha,coleccionAux,(long)5,hechosXprovincia);
    estudioCategoria =
        new ResultadoEstudioCategoria(fecha,"Incendio",5, 12 ,hechosXprovincia);
    estadistico = new Estadistico();

    if(fuenteDinamica.getId() == null) {
      repoFuentes.agregarFuente(fuenteDinamica);
    } else {
      entityManager().merge(fuenteDinamica);
    }

    if (coleccionAux.getId() == null) {
      repoColecciones.save(coleccionAux);
    } else {
      entityManager().merge(coleccionAux);
    }

  }

  @Disabled
  @Test
  public void provinciaConMasHechosDeUnaColleccion() {

    entityManager().persist(estudioColeccion);

    String provincia = estadistico.provinciaConMayorCantidadDeHechosReportadosDeColeccion(coleccionAux,fecha);

    Assertions.assertEquals("LA_PAMPA",provincia);
    System.out.println(provincia);
  }
}
