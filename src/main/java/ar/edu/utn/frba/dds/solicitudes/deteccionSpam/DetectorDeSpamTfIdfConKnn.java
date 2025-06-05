package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

public class DetectorDeSpamTfIdfConKnn implements DetectorDeSpam {
  Vectorizador vectorizador;
  ClasificadorKNN clasificador;

  @Override
  public boolean esSpam(String justificacion) {
    return true;
  }
}
