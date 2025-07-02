package ar.edu.utn.frba.dds.hechos.consenso;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.Set;

public interface AlgoritmoConsenso {
  Set<Hecho> getHechosConsensuados(Set<Fuente> fuentes);
}
