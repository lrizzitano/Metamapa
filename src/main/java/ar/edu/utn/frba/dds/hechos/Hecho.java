package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.execpciones.HechoInvalidoException;
import ar.edu.utn.frba.dds.usuarios.Usuario;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDate;

@Entity
@Table(name = "Hecho")
public record Hecho(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hecho_id")
    Long id,
    @Column(name = "titulo")
    String titulo,
    @Column(name = "descripcion")
    String descripcion,
    @Column(name = "categoria")
    String categoria,
    @Column(name = "latitud")
    Double latitud,
    @Column(name = "longitud")
    Double longitud,
    @Column(name = "fechaCarga")
    LocalDate fechaCarga,
    @Column(name = "fechaAcontecimiento")
    LocalDate fechaAcontecimiento,
    @Enumerated(EnumType.STRING)
    @Column(name = "origen")
    Origen origen,
    @Column(name = "multimedia")
    Path multimedia,
    @ManyToOne()
    @JoinColumn(name = "contribuyente_id")
    Usuario contribuyente) {

  public Hecho {
    this.isNull(titulo, "titulo");
    this.isNull(descripcion, "descripción");
    this.isNull(categoria, "categoría");
    this.isNull(latitud, "latitud");
    this.isNull(longitud, "longitud");
    this.isNull(fechaCarga, "fecha de carga");
    this.isNull(fechaAcontecimiento, "fecha del acontecimiento");
    this.isNull(origen, "origen");
  }

  public Hecho(Long id, String titulo, String descripcion, String categoria, Double latitud,
               Double longitud, LocalDate fechaCarga, LocalDate fechaAcontecimiento,
               Origen origen) {
    this(id, titulo, descripcion, categoria, latitud, longitud, fechaCarga, fechaAcontecimiento,
        origen, null, null);
  }

  private <T> void isNull(T valor, String parametro) {
    if (valor == null) {
      throw new HechoInvalidoException("No se asigno " + parametro);
    }
  }
}
