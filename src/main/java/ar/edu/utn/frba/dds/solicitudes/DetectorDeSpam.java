package ar.edu.utn.frba.dds.solicitudes;

@FunctionalInterface
public interface DetectorDeSpam {
  public boolean esSpam(String justificacion);
}
