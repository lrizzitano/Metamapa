package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.FuenteMock;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.HechoRepository;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import ar.edu.utn.frba.dds.model.execpciones.NoSePudoLeerArchivoException;
import java.io.IOException;

public class SetupData implements WithSimplePersistenceUnit {
  static private final Ubicacion laPampa = new Ubicacion(-25.8666139, -60.5564475, Provincia.LA_PAMPA, null);
  static private final Ubicacion burzaco = new Ubicacion(-34.23, -57.73, Provincia.BUENOS_AIRES, null);
  static private final Ubicacion saladillo = new Ubicacion(-35.65, -59.56, Provincia.BUENOS_AIRES, null);
  static private final Ubicacion sanLuis = new Ubicacion(-32.81, -65.85, Provincia.SAN_LUIS, null);
  static private final Ubicacion cutralco = new Ubicacion(-38.97, -69.61, Provincia.NEUQUEN, null);
  static private final Ubicacion rioChico = new Ubicacion(-47.43, -71.38, Provincia.SANTA_CRUZ, null);

  static private final Hecho hecho1 = new Hecho(
      null,
      "Muerte en La Pampa",
      "Fuertes lluvias provocaron inundaciones en barrios céntricos",
      "Desastre Natural",
      laPampa,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 15, 22, 14),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho2 = new Hecho(
      null,
      "Inundación en La Pampa",
      "Fuertes lluvias provocaron inundaciones en barrios perifericos",
      "Desastre Natural",
      laPampa,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho3 = new Hecho(
      null,
      "Fuego en La Pampa",
      "el derretimiento de los polos inundo la plata",
      "Desastre Natural",
      laPampa,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 15, 14, 30),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho4 = new Hecho(
      null,
      "tornado en burzaco",
      "TORNADO LOCO vuela a señora de su reposera",
      "Desastre Natural",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 9, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho5 = new Hecho(
      null,
      "El chapulin colorado salva una familia en un edificio en llamas",
      "No contabamos con su astucia",
      "Milagro",
      saladillo,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho6 = new Hecho(
      null,
      "Vaso de agua se convierte en fernet 1882",
      "A caballo regalado...",
      "Milagro",
      rioChico,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho7 = new Hecho(
      null,
      "Policia no le pega a jubilado",
      "Se distrajo con un niño del garrahan",
      "Milagro",
      cutralco,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho8 = new Hecho(
      null,
      "Adulto aprende a volar",
      "Esperemos sepa aterrizar",
      "Milagro",
      rioChico,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho9 = new Hecho(
      null,
      "Adulto deja de volar y cae del cielo",
      "parece que no sabía",
      "Milagro",
      sanLuis,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

   private final Fuente fuente8Estatica = crearFuente8Estatica();

  private Fuente crearFuente8Estatica() {
    try {
      var resource = getClass().getClassLoader().getResource("hechosEstatica1.csv");
      if (resource == null) {
        throw new FileNotFoundException("Archivo hechosEstatica1.csv no encontrado en resources");
      }
      return new FuenteEstatica(Paths.get(resource.toURI()).toString());
    } catch (Exception e) {
      throw new RuntimeException("Error cargando fuente8Estatica", e);
    }
  }

  static final Set<Hecho> hechos = Set.of(hecho1, hecho2, hecho3, hecho4, hecho5, hecho6, hecho7, hecho8, hecho9);
  static final FuenteDinamica fuenteDinamica = FuenteDinamica.instance();
  private final Fuente fuente2 = fuente8Estatica;
  private final Fuente fuente3 = fuente8Estatica;
  private final Fuente fuente4 = fuente8Estatica;
  private final Fuente fuente5 = fuente8Estatica;
  private final Fuente fuente6 = fuente8Estatica;
  private final Fuente fuente7 = fuente8Estatica;

  private final Coleccion collecion1 = new Coleccion("Hechos dinamicos", "desc1",
      new NullFiltro(), fuenteDinamica, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion2 = new Coleccion("Coleccion 2", "desc2",
      new NullFiltro(), fuente2, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion3 = new Coleccion("Coleccion 3", "desc2",
      new NullFiltro(), fuente3, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion4 = new Coleccion("Coleccion 4", "desc2",
      new NullFiltro(), fuente4, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion5 = new Coleccion("Coleccion 5", "desc2",
      new NullFiltro(), fuente5, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion6 = new Coleccion("Coleccion 6", "desc2",
      new NullFiltro(), fuente6, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion7 = new Coleccion("Coleccion 7", "desc2",
      new NullFiltro(), fuente7, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion8 = new Coleccion("Estatica",
      "Esta coleccion trae hechos de un csv",
      new NullFiltro(), fuente8Estatica, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Set<Coleccion> colecciones = Set.of(collecion1, collecion2, collecion3, collecion4,
      collecion5, collecion6, collecion7,collecion8);

  public void setup()  {
    hecho1.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.prensalibre.com%2Fwp-content%2Fuploads%2F2022%2F06%2FLluvia-12-2.jpg%3Fquality%3D52&f=1&nofb=1&ipt=31f7daa0a83e74837d4f3c1765b3ca5c424d62fe004a2aea6a806cc0ac235e4c");
    hecho2.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcorrientealterna.unam.mx%2Fwp-content%2Fuploads%2F2022%2F09%2FInundacion-Tula-Hidalgo.jpeg&f=1&nofb=1&ipt=37a989c042665a2f2f8c441dd16c83caea2f1ebac60b430052d2a4b9c472898f");
    hecho3.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia.a24.com%2Fp%2Ff366775f9ca227c56c0afc08fcda2ff1%2Fadjuntos%2F296%2Fimagenes%2F008%2F949%2F0008949932%2F1200x675%2Fsmart%2Festerosdelibera-incendio-corrientesjpg.jpg");
    hecho4.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fd.newsweek.com%2Fen%2Ffull%2F2247380%2Ff-3-tornado.jpg&f=1&nofb=1&ipt=1810131d106a694d37ed01d9a5f89da881c7c371d0ccc6cde0ca97035a9f0fda");
    hecho5.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwallpapercave.com%2Fwp%2Fwp7211327.jpg&f=1&nofb=1&ipt=599941da51a84537203652a0590db799c8ec698d8d06cc126eb2812c365e8cc0");
    hecho6.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftelefe-static2.akamaized.net%2Fmedia%2F458031%2Flaly-y-el-fernet.jpg%3Fv%3D20220915095610000%26format%3Dmain%26width%3D640%26height%3D360%26mode%3Dcrop&f=1");
    hecho7.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.tiempoar.com.ar%2Fwp-content%2Fuploads%2F2025%2F03%2FFoto_20250317_identifican-al-gendarme-que-disparo-contra-pablo-grillo.jpg");
    hecho8.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia.istockphoto.com%2Fid%2F155428311%2Fes%2Ffoto%2Fflying-empresario.jpg%3Fs%3D612x612%26w%3D0%26k%3D20%26c%3DtnotnINdHQE0OUDxbQSJGEKqiml1s2UuLdH5S-2bY-g%3D");
    hecho9.setMultimedia("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.noticiaspuebla.mx%2Fwp-content%2Fuploads%2F2023%2F02%2FMomento-exacto-de-la-caida-de-Edgar.webp&f=1&nofb=1&ipt=3c56a7aa692e55dcfe479b2d86dd5615bccfc3907decfdf88b46ca8bc26476e6");

    withTransaction(() -> {
      HechosFuenteDinamicaJPA repoHechos =new HechosFuenteDinamicaJPA();
      repoHechos.persist(hecho1);
      repoHechos.persist(hecho2);
      repoHechos.persist(hecho3);
      repoHechos.persist(hecho4);

      FuentesRepositoryJPA fuenteRepo = new FuentesRepositoryJPA();
      fuenteRepo.persist(fuenteDinamica);
      fuenteRepo.persist(fuente2);
      fuenteRepo.persist(fuente3);
      fuenteRepo.persist(fuente4);
      fuenteRepo.persist(fuente5);
      fuenteRepo.persist(fuente6);
      fuenteRepo.persist(fuente7);
      fuenteRepo.persist(fuente8Estatica);

      ColeccionesRepository colecRepo = new ColeccionesRepository();
      colecRepo.persist(collecion1);
      colecRepo.persist(collecion2);
      colecRepo.persist(collecion3);
      colecRepo.persist(collecion4);
      colecRepo.persist(collecion5);
      colecRepo.persist(collecion6);
      colecRepo.persist(collecion7);
      colecRepo.persist(collecion8);

      new RepoUsuarios().persist(new Administrador("Ad","a", "Ministrador", LocalDate.now(), "contrasenia"));
    });
  }

}
