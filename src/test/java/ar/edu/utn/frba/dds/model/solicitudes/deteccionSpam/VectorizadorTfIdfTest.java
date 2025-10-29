package ar.edu.utn.frba.dds.model.solicitudes.deteccionSpam;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VectorizadorTfIdfTest {
  private static VectorizadorTfIdf vectorizador;

  @BeforeAll
  public static void configurarVectorizador() {
    List<Solicitud> corpus = LectorDataset
        .cargarSolicitudes("./src/main/java/ar/edu/utn/frba/dds/model/solicitudes/deteccionSpam/dataset.csv");

    List<String> justificaciones = corpus.stream().map(s -> s.justificacion).toList();
    vectorizador = new VectorizadorTfIdf(justificaciones);
  }

  @Test
  void puedeVectorizarUnaPalabra() {
    assertTrue(vectorizador.vectorizar("test").getClass().isArray());
    assertEquals(double.class, vectorizador.vectorizar("test").getClass().getComponentType());
  }

  @Test
  void puedeVectorizarUnTexto() {
    assertTrue(vectorizador.vectorizar("texto de prueba").getClass().isArray());
    assertEquals(double.class, vectorizador.vectorizar("texto de prueba").getClass().getComponentType());
  }

  @Test
  void dosTextosIgualesDanVectorIgual() {
    assertArrayEquals(vectorizador.vectorizar("textito de prueba"),
        vectorizador.vectorizar("textito de prueba"));
  }

  @Test
  void dosTextosDistintosDanVectoresDistintos() {
    assertFalse(Arrays.equals(vectorizador.vectorizar("este hecho tiene discursos de odio"),
        vectorizador.vectorizar("este hecho tiene violencia explicita")));
  }
}
