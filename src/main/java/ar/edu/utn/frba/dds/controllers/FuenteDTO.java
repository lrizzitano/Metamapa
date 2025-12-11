package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;

public class FuenteDTO {
  public Long id;
  public String nombre;
  public String detalle;

  public FuenteDTO(Fuente fuente) {
    this.id = fuente.getId();
    this.nombre = fuente.getNombre();
    this.detalle = fuente.detalle();
  }

  public Long id(){return id;}
  public String nombre(){return nombre;}
  public String detalle(){return detalle;}
}
