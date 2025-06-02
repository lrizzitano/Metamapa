package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.execpciones.HechoInvalidoException;
import ar.edu.utn.frba.dds.usuarios.Usuario;

import java.nio.file.Path;
import java.time.LocalDate;

public record Hecho(String titulo, String descripcion, String categoria, Double latitud,
                    Double longitud, LocalDate fechaCarga, LocalDate fechaAcontecimiento,
                    Origen origen, Path multimedia, Usuario contribuyente) {
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

  public Hecho(String titulo, String descripcion, String categoria, Double latitud,
               Double longitud, LocalDate fechaCarga, LocalDate fechaAcontecimiento,
               Origen origen) {
    this(titulo, descripcion, categoria, latitud, longitud, fechaCarga, fechaAcontecimiento,
        origen, null, null);
  }

  private <T> void isNull(T valor, String parametro) {
    if (valor == null) {
      throw new HechoInvalidoException("No se asigno " + parametro);
    }
  }
}
