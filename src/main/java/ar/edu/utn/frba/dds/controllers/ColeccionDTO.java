package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.hechos.Coleccion;

public class ColeccionDTO {
  private final long id;
  private final String titulo;
  private final String descripcion;
  private final String fuente;
  private final String consenso;

  private final String fechaDesde;
  private final String fechaHasta;
  private final String categoria;

  public ColeccionDTO(Coleccion coleccion) {
    this.id=coleccion.getId();
    this.titulo = coleccion.getTitulo();
    this.descripcion = coleccion.getDescripcion();
    this.fuente = coleccion.getFuente().getNombre();
    this.consenso = "";
    this.fechaDesde = "a";
    this.fechaHasta = "b";
    this.categoria = "c";
  }

  public Long id(){return id;}
  public String titulo(){return titulo;}
  public String descripcion(){return descripcion;}
  public String fuente(){return fuente;}
  public String consenso(){return consenso;}
  public String fechaDesde(){return fechaDesde;}
  public String fechaHasta(){return fechaHasta;}
  public String categoria(){return categoria;}

}
