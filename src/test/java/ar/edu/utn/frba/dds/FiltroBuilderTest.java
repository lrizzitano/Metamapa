package ar.edu.utn.frba.dds;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FiltroTest {
  private final FiltroBuilder builder = new FiltroBuilder();
  private final Predicate<Hecho> filtro1 = hecho -> hecho.getTitulo().contains("incendio");
  private final Predicate<Hecho> filtro2 = hecho -> hecho.getFechaCarga().equals(LocalDate.now());
  private final Hecho hecho1 = mock(Hecho.class);
  private final Hecho hecho2 = mock(Hecho.class);
  private final Set<Hecho> hechos = new HashSet<>();

  @BeforeEach
  void setUp() {
    when(hecho1.getTitulo()).thenReturn("incendio forestal");
    when(hecho2.getTitulo()).thenReturn("destruccion forestal");
    when(hecho1.getFechaCarga()).thenReturn(LocalDate.now());
    when(hecho2.getFechaCarga()).thenReturn(LocalDate.now());
    hechos.add(hecho1);
    hechos.add(hecho2);
  }

  @Test
  void seFiltraLaLista(){
    builder.añadirFiltro(filtro1).añadirFiltro(filtro2);
    Predicate<Hecho> filtro = builder.obtenerFiltro();
    Assertions.assertEquals(1, hechos.stream().filter(filtro).toList().size());
  }

  @Test
  void añadirUnFiltroTrueNoAfectaNada(){
    builder.añadirFiltro(filtro1).añadirFiltro(filtro2).añadirFiltro(hecho -> true);
    Predicate<Hecho> filtro = builder.obtenerFiltro();
    Assertions.assertEquals(1, hechos.stream().filter(filtro).toList().size());
  }

  @Test
  void unFiltroVacioNoAfectaLaLista(){
    Predicate<Hecho> filtro =  builder.obtenerFiltro();
    Assertions.assertEquals(2, hechos.stream().filter(filtro).toList().size());
  }
}
