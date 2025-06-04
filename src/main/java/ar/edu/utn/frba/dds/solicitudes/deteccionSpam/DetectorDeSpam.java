package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

@FunctionalInterface
public interface DetectorDeSpam {
  public boolean esSpam(String justificacion);
}
