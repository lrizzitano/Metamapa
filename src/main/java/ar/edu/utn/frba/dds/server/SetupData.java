package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.hechos.Origen;
import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.Ubicacion;
import ar.edu.utn.frba.dds.model.hechos.consenso.ConsensoNull;
import ar.edu.utn.frba.dds.model.repositorios.ColeccionesRepository;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.solicitudes.SolicitudesDeEliminacionJPA;
import ar.edu.utn.frba.dds.model.fuentes.FuenteMock;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.Set;

public class SetupData implements WithSimplePersistenceUnit {
  static private final Ubicacion laPampa = new Ubicacion(12.2,12.2, Provincia.LA_PAMPA,null);
  static private final Ubicacion burzaco = new Ubicacion(12.2, 12.2, Provincia.BUENOS_AIRES,null);

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
      "pobre cpu 10",
      "Desastre Natural",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2024, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho5 = new Hecho(
      null,
      "cpu 10 salva una familia en un edificio en llamas",
      "grande cpu 10",
      "Milagro",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho6 = new Hecho(
      null,
      "hiurgvhgbniuoesa5",
      "grande cpu 10",
      "Milagro",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho7 = new Hecho(
      null,
      "ggere",
      "grande cpu 10",
      "Milagro",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho8 = new Hecho(
      null,
      "Adulto aprende a volar",
      "grande cpu 10",
      "Milagro",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static private final Hecho hecho9 = new Hecho(
      null,
      "Adulto deja de volar y cae del cielo",
      "grande cpu 10",
      "Milagro",
      burzaco,
      LocalDateTime.now(),
      LocalDateTime.of(2023, 3, 15, 23, 59),
      Origen.CONTRIBUYENTE
  );

  static final Set<Hecho> hechos = Set.of(hecho1, hecho2, hecho3, hecho4, hecho5, hecho6, hecho7);
  static final Fuente fuente1 = new FuenteMock(Set.of(hecho1, hecho2, hecho3));
  static final Fuente fuente2 = new FuenteMock(Set.of(hecho4, hecho5, hecho6, hecho7,hecho8,hecho9));
  static final Coleccion collecion1 = new Coleccion("Coleccion 1", "desc1",
      new NullFiltro(),  fuente1,new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  static final Coleccion collecion2 = new Coleccion("Coleccion 2", "desc2",
      new NullFiltro(),  fuente2,new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  static final Coleccion collecion3 = new Coleccion("Coleccion 3", "desc2",
      new NullFiltro(),  fuente2,new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  static final Coleccion collecion4 = new Coleccion("Coleccion 4", "desc2",
      new NullFiltro(),  fuente2,new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  static final Coleccion collecion5 = new Coleccion("Coleccion 5", "desc2",
      new NullFiltro(),  fuente2,new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  static final Coleccion collecion6 = new Coleccion("Coleccion 6", "desc2",
      new NullFiltro(),  fuente2,new ConsensoNull(), new SolicitudesDeEliminacionJPA());
  static final Coleccion collecion7 = new Coleccion("Coleccion 7", "desc2",
      new NullFiltro(),  fuente2,new ConsensoNull(), new SolicitudesDeEliminacionJPA());

  static final Set<Coleccion> colecciones = Set.of(collecion1, collecion2,collecion3,collecion4,
      collecion5,collecion6,collecion7);

  public void setup() {
    withTransaction(() -> {
      FuentesRepositoryJPA fuenteRepo = new FuentesRepositoryJPA();
      fuenteRepo.persist(fuente1);
      fuenteRepo.persist(fuente2);

      ColeccionesRepository colecRepo = new ColeccionesRepository();
      colecRepo.persist(collecion1);
      colecRepo.persist(collecion2);
      colecRepo.persist(collecion3);
      colecRepo.persist(collecion4);
      colecRepo.persist(collecion5);
      colecRepo.persist(collecion6);
      colecRepo.persist(collecion7);
    });
  }
}
