package ar.edu.utn.frba.dds;

import java.time.LocalDate;

public class Hecho{

  private final String titulo;
  private final String descripcion;
  private final String categoria;
  private final String ubicacion;
  private final LocalDate fechaCarga;
  private final LocalDate fechaAcontecimiento;
  private final Origen origen;
  //private TipoHecho tipo; MULTIMEDIA o CONTRIBUYENTE

  public Hecho(String titulo,String descripcion,String categoria,String ubicacion,LocalDate fechaCarga,
               LocalDate fechaAcontecimiento,Origen origen){

    if(titulo==null){throw new HechoInvalidoException("No se asigno ningun Titulo");}
    if(descripcion==null){throw new HechoInvalidoException("No se asigno ninguna Descripcion");}
    if(categoria==null){throw new HechoInvalidoException("No se asigno ninguna Categoria");}
    if(ubicacion==null){throw new HechoInvalidoException("No se asigno ninguna Ubicacion");}
    if(fechaCarga==null){throw new HechoInvalidoException("No se asigno ninguna Fecha de Carga");}
    if(fechaAcontecimiento==null){throw new HechoInvalidoException("No se asigno ninguna Fecha de Acontecimiento");}
    if(origen==null){throw new HechoInvalidoException("No se asigno ningun origen");}

    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.ubicacion = ubicacion;
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
