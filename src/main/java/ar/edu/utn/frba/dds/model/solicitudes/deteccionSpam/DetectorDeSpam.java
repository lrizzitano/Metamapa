package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

@FunctionalInterface
public interface DetectorDeSpam {
  boolean esSpam(String justificacion);
}
