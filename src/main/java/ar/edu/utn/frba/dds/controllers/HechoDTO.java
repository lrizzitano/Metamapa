package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HechoDTO {
  private final Long id;
  private final String titulo;
  private final String descripcion;
  private final String categoria;
  private final double latitud;
  private final double longitud;
  private final String fechaAcontecimiento;
  private final String fechaCarga;
  private final String origen;
  private final String multimedia;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

  public HechoDTO(Hecho hecho) {
    this.id = hecho.id();
    this.titulo = hecho.titulo();
    this.descripcion = hecho.descripcion();
    this.categoria = hecho.categoria();
    this.longitud = hecho.longitud();
    this.latitud = hecho.latitud();
    this.fechaAcontecimiento = hecho.fechaAcontecimiento().format(formatter);
    this.fechaCarga = hecho.fechaCarga().format(formatter);
    this.origen = hecho.origen().name();
    this.multimedia = hecho.multimedia();
  }

  public Long id() {
    return id;
  }
  public String titulo() {
    return titulo;
  }
  public String descripcion() {
    return descripcion;
  }
  public String categoria() {
    return categoria;
  }
  public double latitud() {
    return latitud;
  }
  public double longitud() {
    return longitud;
  }
  public String fechaAcontecimiento() {
    return fechaAcontecimiento;
  }
  public String fechaCarga() {
    return fechaCarga;
  }
  public String origen() {
    return origen;
  }
  public String multimedia() {
    return multimedia;
  }
  public DateTimeFormatter formatter() {
    return formatter;
  }
}
