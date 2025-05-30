package ar.edu.utn.frba.dds.Solicitudes;

@FunctionalInterface
public interface DetectorDeSpam {
  public boolean esSpam(String justificacion);
}
