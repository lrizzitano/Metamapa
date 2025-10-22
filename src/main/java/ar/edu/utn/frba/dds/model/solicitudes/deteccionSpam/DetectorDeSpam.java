package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

@FunctionalInterface
public interface DetectorDeSpam {
  boolean esSpam(String justificacion);
}
