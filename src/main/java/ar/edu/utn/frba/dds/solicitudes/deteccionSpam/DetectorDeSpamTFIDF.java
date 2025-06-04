package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;

public class DetectorDeSpamTFIDF implements DetectorDeSpam {
  Vectorizador vectorizador;
  //Clasificador clasificador;

  @Override
  public boolean esSpam(String justificacion) {
    return true;
  }
}
