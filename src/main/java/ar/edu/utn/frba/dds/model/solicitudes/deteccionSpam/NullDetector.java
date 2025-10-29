package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

public class NullDetector implements DetectorDeSpam{
  @Override
  public boolean esSpam(String justificacion) {
    return false;
  }
}
