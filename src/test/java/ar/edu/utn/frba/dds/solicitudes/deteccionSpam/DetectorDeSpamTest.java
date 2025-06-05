package ar.edu.utn.frba.dds.solicitudes.deteccionSpam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DetectorDeSpamTest {
  static DetectorDeSpam detectorDeSpam;

  @BeforeAll
  static void configurarDetector() {
    List<Solicitud> solicitudes = LectorDataset
        .cargarSolicitudes("./src/main/java/ar/edu/utn/frba/dds/solicitudes/deteccionSpam/dataset.csv");

    detectorDeSpam = new DetectorDeSpamTfIdfConKnn(solicitudes, 5);
  }

  @Test
  void detectaMensajeDeSpamPresenteEnDataset() {
    assertTrue(detectorDeSpam.esSpam("Compra seguidores para tu perfil, promocion limitada"));
  }

  @Test
  void detectaMensajeRealPresenteEnDataset() {
    assertFalse(detectorDeSpam.esSpam("Hola, por favor eliminen esta publicación, contiene información falsa."));
  }

  @Test
  void detectaMensajeDeSpamNuevo() {
    assertTrue(detectorDeSpam.esSpam("Gana dinero facil y sin vueltas"));
  }

  @Test
  void detectaMensajeRealNuevo() {
    assertFalse(detectorDeSpam.esSpam("Este hecho promueve discursos racistas"));
  }
}
