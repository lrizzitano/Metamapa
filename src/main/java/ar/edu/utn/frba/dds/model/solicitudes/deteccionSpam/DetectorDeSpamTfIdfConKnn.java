package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

import java.util.List;

public class DetectorDeSpamTfIdfConKnn implements DetectorDeSpam {
  VectorizadorTfIdf vectorizador;
  ClasificadorKnn clasificador;

  DetectorDeSpamTfIdfConKnn(List<Solicitud> corpusDeEjemplo, int cantidadDeVecinos) {
    List<String> justificaciones = corpusDeEjemplo.stream().map(s -> s.justificacion).toList();
    this.vectorizador = new VectorizadorTfIdf(justificaciones);

    List<VectorEtiquetado> vectoresEtiquetados = corpusDeEjemplo.stream()
        .map(s -> new VectorEtiquetado(vectorizador.vectorizar(s.justificacion), s.etiqueta)).toList();

    this.clasificador = new ClasificadorKnn(vectoresEtiquetados, cantidadDeVecinos);
  }

  @Override
  public boolean esSpam(String justificacion) {
    return clasificador.esDeCategoria(Categoria.SPAM, vectorizador.vectorizar(justificacion));
  }
}