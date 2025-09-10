package ar.edu.utn.frba.dds.hechos;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.filtros.FiltroFechaHasta;
import ar.edu.utn.frba.dds.filtros.NullFiltro;
import ar.edu.utn.frba.dds.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.hechos.consenso.ConsensoNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ColeccionTest{
  private final Hecho hecho1 = new Hecho(null,"hecho1", "desc1", "cat1",
      1.0, 2.0,  LocalDate.now(), LocalDate.parse("2024-01-01"), Origen.DATASET);
  private final Hecho hecho2 = new Hecho(null,"hecho2", "desc2", "cat2",
      3.0,4.0, LocalDate.now(), LocalDate.parse("2024-01-02"), Origen.DATASET);
  private final Fuente unaFuente = mock(Fuente.class);
  private final Filtro filtroFecha = new FiltroFechaHasta(LocalDate.parse("2024-01-02"));
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