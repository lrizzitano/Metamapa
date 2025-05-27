package ar.edu.utn.frba.dds;

@FunctionalInterface
public interface DetectorDeSpam {
  public boolean esSpam(String justificacion);
}
