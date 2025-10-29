package ar.edu.utn.frba.dds.model.hechos;


import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ColeccionTest{
  private final Hecho hecho1 = new Hecho(null,"hecho1", "desc1", "cat1",
      new Ubicacion(1.0, 2.0, null, null),  LocalDateTime.now(), LocalDate.parse("2024-01-01").atStartOfDay(), Origen.DATASET);
  private final Hecho hecho2 = new Hecho(null,"hecho2", "desc2", "cat2",
      new Ubicacion(3.0, 4.0, null, null), LocalDateTime.now(), LocalDate.parse("2024-01-02").atStartOfDay(), Origen.DATASET);
  private final Fuente unaFuente = mock(Fuente.class);
  private final Filtro filtroFecha = new FiltroFechaHasta(LocalDate.parse("2024-01-02").atStartOfDay());
  private final Filtro filtroTrue = new NullFiltro();

  private final SolicitudesDeEliminacionJPA solicitudesRepo = new SolicitudesDeEliminacionJPA();
  private final HechosFuenteDinamicaJPA hechos = new HechosFuenteDinamicaJPA();


  @BeforeEach
  void setUp() {

    Set<Hecho> hechos = Set.of(hecho1, hecho2);
    when(unaFuente.obtenerHechos(any())).thenAnswer(invocation -> {
      Filtro filtro = invocation.getArgument(0);
      return hechos.stream().filter(filtro.getAsPredicate()).collect(Collectors.toSet());
    });
  }

  @Test
  void noLeImportaElConsensoSiNoSeLePide() {
    Consenso criterioConsenso = mock(Consenso.class);
    when(criterioConsenso.getHechosConsensuados()).thenReturn(new HashSet<>());
    Coleccion unaColeccion = new Coleccion("", "",
        filtroTrue, unaFuente, criterioConsenso, solicitudesRepo);
    Assertions.assertEquals(Set.of(hecho1, hecho2), unaColeccion.hechos(filtroTrue));
  }



  @Test
  void filtraPorConsenso() {
    Consenso criterioConsenso = mock(Consenso.class);
    when(criterioConsenso.esConsensuado(hecho1)).thenReturn(true);
    when(criterioConsenso.esConsensuado(hecho2)).thenReturn(false);
    Coleccion unaColeccion = new Coleccion("", "",
        filtroTrue, unaFuente, criterioConsenso, solicitudesRepo);
    Assertions.assertEquals(Set.of(hecho1), unaColeccion.hechosConsensuados(filtroTrue));
  }

  @Test
  void ColeccionFiltraPorCriterioDePertenencia() {
    Coleccion unaColeccion = new Coleccion("", "",
        filtroFecha, unaFuente, new ConsensoNull(), solicitudesRepo);
    Assertions.assertEquals(Set.of(hecho1), unaColeccion.hechos(filtroTrue));
  }

  @Test
  void ColeccionFiltraPorParametro() {
    Coleccion unaColeccion = new Coleccion("", "", filtroTrue, unaFuente,
        new ConsensoNull(), solicitudesRepo);
    Assertions.assertEquals(unaColeccion.hechos(filtroFecha),
        Set.of(hecho1));
  }


}