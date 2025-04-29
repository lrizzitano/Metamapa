package ar.edu.utn.frba.dds;

@FunctionalInterface
public interface Filtro {
  boolean aplicar(Hecho hecho);
}
