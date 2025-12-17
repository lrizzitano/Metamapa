package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.calendarizables.ActualizadorEstadisticas;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.FuenteMetaMapa;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoConsensoAbsoluto;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoMayoriaSimple;
import ar.edu.utn.frba.dds.model.hechos.consenso.AlgoritmoMultiplesMenciones;
import ar.edu.utn.frba.dds.model.hechos.consenso.Consenso;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensosRepository;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.HechosFuenteDinamicaJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoUsuarios;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.RechazosDeEliminacion;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudDeCambioRepository;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudDeEliminacionRepository;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SetupData implements WithSimplePersistenceUnit {
  static private final Ubicacion laPampa = new Ubicacion(-25.8666139, -60.5564475, Provincia.LA_PAMPA, null);
  static private final Ubicacion burzaco = new Ubicacion(-34.23, -57.73, Provincia.BUENOS_AIRES, null);

  static private final Hecho hecho1 = new Hecho(
      null,
      "Muerte en La Pampa",
      "Fuertes lluvias provocaron inundaciones en barrios céntricos",
      "Desastre Natural",
      laPampa,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 12, 22, 14),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho2 = new Hecho(
      null,
      "Inundación en La Pampa",
      "Fuertes lluvias provocaron inundaciones en barrios perifericos",
      "Desastre Natural",
      laPampa,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 13, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho3 = new Hecho(
      null,
      "Fuego en La Pampa",
      "el derretimiento de los polos inundo la plata",
      "Desastre Natural",
      laPampa,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 14, 14, 30),
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

  private Fuente crearFuenteEstatica(String archivo) {
    try {
      var resource = getClass().getClassLoader().getResource(archivo);
      if (resource == null) {
        throw new FileNotFoundException("Archivo recitales.csv no encontrado en resources");
      }
      return new FuenteEstatica(Paths.get(resource.toURI()).toString());
    } catch (Exception e) {
      throw new RuntimeException("Error cargando fuente8Estatica", e);
    }
  }

  static final FuenteDinamica fuenteDinamica = FuenteDinamica.instance();
  private final Fuente fuenteEstatica1 = crearFuenteEstatica("incendios.csv");
  private final Fuente fuenteEstatica2 = crearFuenteEstatica("manifestaciones.csv");
  private final Fuente fuenteEstatica3 = crearFuenteEstatica("recitales.csv");
  private final Fuente fuenteProxy = new FuenteMetaMapa("http://localhost:8081");
  // aca asumimos que hay otra instancia en ese puerto, despues habria q cambiar a otro deploy


  private final Coleccion collecion1 = new Coleccion("Hechos dinamicos", "desc1",
      new NullFiltro(), fuenteDinamica, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion2 = new Coleccion("Incendios", "Coleccion con incendios",
      new NullFiltro(), fuenteEstatica1, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion3 = new Coleccion("Manifestaciones", "Coleccion con manifestaciones",
      new NullFiltro(), fuenteEstatica2, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion collecion4 = new Coleccion("Recitales", "Coleccion con recitales",
      new NullFiltro(), fuenteEstatica3, new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  private final Coleccion coleccionProxy = new Coleccion("Proxy", "coleccion con todos los hechos del otro sistema",
      new NullFiltro(), fuenteProxy, new ConsensoNull(), new SolicitudesDeEliminacionJPA());


  SolicitudDeEliminacion solicitudDeEliminacion1 = new SolicitudDeEliminacion("banquete", "muy malo1");
  SolicitudDeEliminacion solicitudDeEliminacion2 = new SolicitudDeEliminacion("banquete", "muy malo2");
  SolicitudDeEliminacion solicitudDeEliminacion3 = new SolicitudDeEliminacion("banquete", "muy malo3");
  SolicitudDeEliminacion solicitudDeEliminacion4 = new SolicitudDeEliminacion("recital ricotero", "muy malo4");
  SolicitudDeEliminacion solicitudDeEliminacion5 = new SolicitudDeEliminacion("recital ricotero", "muy malo5");
  SolicitudDeEliminacion solicitudDeEliminacion6 = new SolicitudDeEliminacion("recital ricotero", "muy malo6");
  SolicitudDeEliminacion solicitudDeEliminacion7 = new SolicitudDeEliminacion("recital ricotero", "muy malo7");
  SolicitudDeEliminacionRepository repoSolis = new SolicitudesDeEliminacionJPA();



  public void setup()  {
    // estos son los "iniciales" de la dinamica, podrian eliminarse y solo tener los subidos a mano
    hecho1.setImagen("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.prensalibre.com%2Fwp-content%2Fuploads%2F2022%2F06%2FLluvia-12-2.jpg%3Fquality%3D52&f=1&nofb=1&ipt=31f7daa0a83e74837d4f3c1765b3ca5c424d62fe004a2aea6a806cc0ac235e4c");
    hecho2.setImagen("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcorrientealterna.unam.mx%2Fwp-content%2Fuploads%2F2022%2F09%2FInundacion-Tula-Hidalgo.jpeg&f=1&nofb=1&ipt=37a989c042665a2f2f8c441dd16c83caea2f1ebac60b430052d2a4b9c472898f");
    hecho3.setImagen("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia.a24.com%2Fp%2Ff366775f9ca227c56c0afc08fcda2ff1%2Fadjuntos%2F296%2Fimagenes%2F008%2F949%2F0008949932%2F1200x675%2Fsmart%2Festerosdelibera-incendio-corrientesjpg.jpg");
    hecho4.setImagen("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fd.newsweek.com%2Fen%2Ffull%2F2247380%2Ff-3-tornado.jpg&f=1&nofb=1&ipt=1810131d106a694d37ed01d9a5f89da881c7c371d0ccc6cde0ca97035a9f0fda");

    withTransaction(() -> {
      // setup de hechos dinamicos iniciales
      HechosFuenteDinamicaJPA repoHechos =new HechosFuenteDinamicaJPA();
      repoHechos.persist(hecho1);
      repoHechos.persist(hecho2);
      repoHechos.persist(hecho3);
      repoHechos.persist(hecho4);

      // setup de fuentes iniciales
      FuentesRepositoryJPA fuenteRepo = new FuentesRepositoryJPA();
      fuenteRepo.persist(fuenteDinamica);
      fuenteRepo.persist(fuenteEstatica1);
      fuenteRepo.persist(fuenteEstatica2);
      fuenteRepo.persist(fuenteEstatica3);
      fuenteRepo.persist(fuenteProxy);

      // colecciones iniciales
      ColeccionesRepository colecRepo = new ColeccionesRepository();
      colecRepo.persist(collecion1);
      colecRepo.persist(collecion2);
      colecRepo.persist(collecion3);
      colecRepo.persist(collecion4);
      colecRepo.persist(coleccionProxy);

      // setup de rechazos para demo estadisticas
      repoSolis.nuevaSolicitud(solicitudDeEliminacion1);
      repoSolis.nuevaSolicitud(solicitudDeEliminacion2);
      repoSolis.nuevaSolicitud(solicitudDeEliminacion3);
      repoSolis.nuevaSolicitud(solicitudDeEliminacion4);
      repoSolis.nuevaSolicitud(solicitudDeEliminacion5);
      repoSolis.nuevaSolicitud(solicitudDeEliminacion6);
      repoSolis.nuevaSolicitud(solicitudDeEliminacion7);

      repoSolis.rechazarSolicitud(solicitudDeEliminacion1);
      repoSolis.rechazarSolicitud(solicitudDeEliminacion2);
      repoSolis.rechazarSolicitud(solicitudDeEliminacion3);
      repoSolis.rechazarSolicitud(solicitudDeEliminacion4);
      repoSolis.rechazarSolicitud(solicitudDeEliminacion5);
      repoSolis.rechazarSolicitud(solicitudDeEliminacion6);
      repoSolis.rechazarSolicitud(solicitudDeEliminacion7);

      RechazosDeEliminacion rechazoRecitalRicotero = entityManager().createQuery(
              "FROM RechazosDeEliminacion r WHERE r.tituloHecho = :hecho", RechazosDeEliminacion.class)
          .setParameter("hecho", "recital ricotero")
          .getResultStream()
          .findFirst()
          .orElse(null);

      if (rechazoRecitalRicotero != null) {
        rechazoRecitalRicotero.sumarSpam();
        rechazoRecitalRicotero.sumarSpam();
      }

      // los 3 tipos de consenso que seran siempre los mismos y actualizados con CRON
      ConsensosRepository consensosRepo = new ConsensosRepository();
      LocalDateTime next = LocalDate.now().plusDays(1).atStartOfDay().plusHours(1);
      consensosRepo.save(new Consenso(new AlgoritmoConsensoAbsoluto(), next, fuenteRepo));
      consensosRepo.save(new Consenso(new AlgoritmoMayoriaSimple(), next, fuenteRepo));
      consensosRepo.save(new Consenso(new AlgoritmoMultiplesMenciones(), next, fuenteRepo));

      // primer corrida de las estadisticas, las proximas serian CRONEADAS
      ActualizadorEstadisticas.main(new String[] {});

      // registro del usuario admin
      new RepoUsuarios().persist(new Administrador("Ad","Pablo", "Gabarini", LocalDate.now(), "contrasenia"));
    });
  }

}
