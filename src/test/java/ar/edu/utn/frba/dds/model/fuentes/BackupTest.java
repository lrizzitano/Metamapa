package ar.edu.utn.frba.dds.model.fuentes;


import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.LocalDateAdapter;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.LocalDateTimeAdapter;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.PathAdapter;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.repositorios.HechoRepository;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BackupTest {
  private Path tempFile;

  @BeforeEach
  void setUp() throws Exception {
    tempFile = Files.createTempFile("backup-test", ".json");
    Files.deleteIfExists(tempFile);
  }

  @AfterEach
  void tearDown() throws Exception {
    Files.deleteIfExists(tempFile);
  }

  @Test
  void lanzaExcepcionSiElArchivoPathNoEsValido() {
    Backup backup = new Backup(Path.of("hola"), LocalDateTime.now(), Duration.ZERO);
    Assertions.assertThrows(RuntimeException.class, backup::actualizar);
  }

  @Test
  void testBackupWritesCorrectHechosJson() throws Exception {
    HechoRepository repo = mock(HechoRepository.class);
    Hecho hecho1 = new Hecho(null,"titulo1", "desc1", "cat1",
    new Ubicacion(1.0, 2.0, null, null),
        LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay(), Origen.DATASET);
   Hecho hecho2 = new Hecho(null,"titulo2", "desc2", "cat2",
       new Ubicacion(2.0, 4.0, null, null),
       LocalDate.now().atStartOfDay().plusDays(1), LocalDate.now().atStartOfDay().minusDays(3),
       Origen.DATASET);

    when(repo.obtenerHechos(any(NullFiltro.class))).thenReturn(Set.of(hecho1, hecho2));
    FuenteDinamica.instance().setHechoRepository(repo);

    Backup backup = new Backup(tempFile, LocalDate.now().atStartOfDay(), Duration.ZERO);
    backup.actualizar();
    String json = Files.readString(tempFile);

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .setPrettyPrinting()
        .create();

    Type setType = new TypeToken<Set<Hecho>>() {}.getType();
    Set<Hecho> hechos = gson.fromJson(json, setType);

    Assertions.assertEquals(Set.of(hecho1, hecho2), hechos);
  }

  @Test
  void estoNoEsUnTest() {
    HechoRepository repo = mock(HechoRepository.class);
    Hecho hecho1 = new Hecho(null,"titulo1", "desc1", "cat1",
        new Ubicacion(1.0, 2.0, null, null),
        LocalDateTime.now(), LocalDateTime.now(), Origen.DATASET);
    Hecho hecho2 = new Hecho(null,"titulo2", "desc2", "cat2",
        new Ubicacion(2.0, 4.0, null, null),
        LocalDateTime.now().plusDays(1), LocalDateTime.now().minusDays(3),
        Origen.DATASET);
    when(repo.obtenerTodos()).thenReturn(Set.of(hecho1, hecho2));
    FuenteDinamica.instance().setHechoRepository(repo);
    Backup backup = new Backup(Path.of("./test.json"), LocalDateTime.now(), Duration.ZERO);
    backup.actualizar();
  }
}