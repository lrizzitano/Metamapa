package ar.edu.utn.frba.dds.model.persistencia;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam.NullDetector;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColeccionesRepositoryTest implements SimplePersistenceTest {

  private final Hecho hecho1 = new Hecho(null,"hecho1", "desc1", "cat1",
      new Ubicacion(1.0, 2.0, null, null),  LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);
  private final Hecho hecho2 = new Hecho(null,"hecho2", "desc2", "cat2",
      new Ubicacion(3.0, 4.0, null, null), LocalDateTime.now(), LocalDate.parse("2024-01-02").atStartOfDay(), Origen.DATASET);
  private final Fuente unaFuente = mock(Fuente.class);
  private final Filtro filtroFecha = new FiltroFechaHasta(LocalDate.parse("2024-01-02").atStartOfDay());
  private final Filtro filtroTrue = new NullFiltro();

  private final RepoUsuarios repoUsuarios = new RepoUsuarios();
  private SolicitudDeEliminacion solicitud =  new SolicitudDeEliminacion(hecho1.titulo(), "null");
  private Administrador administrador = new Administrador("a", "a", "a", LocalDate.now(), "a");

  private final SolicitudesDeEliminacionJPA solicitudesRepo = new SolicitudesDeEliminacionJPA();
  private final HechosFuenteDinamicaJPA hechos = new HechosFuenteDinamicaJPA();



  //ANDANDO
  @BeforeEach
  void setUp() {
    hechos.agregar(hecho1);
    repoUsuarios.save(administrador);
    solicitudesRepo.setDetectorDeSpam(new NullDetector());
    solicitudesRepo.nuevaSolicitud(solicitud);

    Set<Hecho> hechos = Set.of(hecho1, hecho2);
    when(unaFuente.obtenerHechos(any())).thenAnswer(invocation -> {
      Filtro filtro = invocation.getArgument(0);
      return hechos.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());
    });
  }

  @Test
  void CollecionFiltraHechosEliminados() {

    solicitud.aceptar(administrador);

    Coleccion unaColeccion = new Coleccion("", "", filtroTrue, unaFuente,
        new ConsensoNull(), solicitudesRepo);

    Set<Hecho> hechosFiltrados = unaColeccion.hechos(filtroTrue);

    Assertions.assertEquals(1, hechosFiltrados.size());
    Assertions.assertEquals(Set.of(hecho2), hechosFiltrados);
  }

}
