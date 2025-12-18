package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.fuentes.Agregador;
import ar.edu.utn.frba.dds.model.hechos.Coleccion;
import java.util.Arrays;
import java.util.List;

public class ColeccionDTO {
  private final long id;
  private final String titulo;
  private final String descripcion;
  private final String fuente;
  private final String consenso;
  private final List<String> criterioPertenencia;
  private final long fuenteID;
  private final boolean tieneAgregador;
  private final List<FuenteDTO> fuentes;

  public ColeccionDTO(Coleccion coleccion) {
    this.id=coleccion.getId();
    this.titulo = coleccion.getTitulo();
    this.descripcion = coleccion.getDescripcion();
    this.fuente = coleccion.getFuente().getNombre();
    this.consenso = coleccion.getConsenso().getNombre();
    this.criterioPertenencia = desglosarNombres(coleccion.getCriterioDePertenencia().getNombre());
    this.fuenteID = coleccion.getFuente().getId();

    this.tieneAgregador = coleccion.getFuente() instanceof Agregador;
    if (this.tieneAgregador) {
      fuentes = ((Agregador) coleccion.getFuente()).fuentes.stream().map(FuenteDTO::new).toList();
    } else {
      fuentes = List.of(new FuenteDTO(coleccion.getFuente()));
    }
  }

  public Long id(){return id;}
  public String titulo(){return titulo;}
  public String descripcion(){return descripcion;}
  public String fuente(){return fuente;}
  public String consenso(){return consenso;}
  public List<String> criterioPertenencia(){return criterioPertenencia;}
  public long fuenteID(){return fuenteID;}
  public List<FuenteDTO> fuentes() { return this.fuentes; }
  public boolean tieneAgregador() { return this.tieneAgregador; }

  public List<String> desglosarNombres(String nombreCompuesto) {
    String[] partes = nombreCompuesto.split(" \\$ ");
    return Arrays.asList(partes);
  }
}
