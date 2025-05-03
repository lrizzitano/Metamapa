package ar.edu.utn.frba.dds;

import java.time.LocalDate;

public class Hecho{

  private final String titulo;
  private final String descripcion;
  private final String categoria;
  private final Double latitud;
  private final Double longitud;
  private final LocalDate fechaCarga;
  private final LocalDate fechaAcontecimiento;
  private final Origen origen;
  //private TipoHecho tipo; MULTIMEDIA o CONTRIBUYENTE

  public Hecho(String titulo,String descripcion,String categoria, Double latitud, Double longitud,LocalDate fechaCarga,
               LocalDate fechaAcontecimiento,Origen origen){

    if(titulo==null){throw new HechoInvalidoException("No se asigno ningun titulo");}
    if(descripcion==null){throw new HechoInvalidoException("No se asigno ninguna descripcion");}
    if(categoria==null){throw new HechoInvalidoException("No se asigno ninguna categoria");}
    if(latitud==null){throw new HechoInvalidoException("No se asigno ninguna latitud");}
    if(longitud==null){throw new HechoInvalidoException("No se asigno ninguna longitud");}
    if(fechaCarga==null){throw new HechoInvalidoException("No se asigno ninguna fecha de carga");}
    if(fechaAcontecimiento==null){throw new HechoInvalidoException("No se asigno ninguna fecha de acontecimiento");}
    if(origen==null){throw new HechoInvalidoException("No se asigno ningun origen");}

    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fechaCarga = fechaCarga;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.origen = origen;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public String getCategoria() {
    return this.categoria;
  }
}
