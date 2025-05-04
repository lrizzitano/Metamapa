package ar.edu.utn.frba.dds;

import java.time.LocalDate;

public record Hecho(String titulo, String descripcion, String categoria, Double latitud,
                    Double longitud,LocalDate fechaCarga, LocalDate fechaAcontecimiento,
                    Origen origen){
  //private TipoHecho tipo; MULTIMEDIA o CONTRIBUYENTE

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

  private <T> void isNull(T valor, String parametro){
    if (valor == null)
      throw new HechoInvalidoException("No se asigno " + parametro);
  }
}
