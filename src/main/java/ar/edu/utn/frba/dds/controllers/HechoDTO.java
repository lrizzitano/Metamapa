package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
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
  private final String video;
  private final String imagen;
  private final String provincia;
  private final String contribuyente;
  private final String fechaAcontecimientoSinFormatear;

  public HechoDTO(Hecho hecho) {
    this.id = hecho.id();
    this.titulo = hecho.titulo();
    this.descripcion = hecho.descripcion();
    this.categoria = hecho.categoria();
    this.longitud = hecho.longitud();
    this.latitud = hecho.latitud();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    this.fechaAcontecimiento = hecho.fechaAcontecimiento().format(formatter);
    this.fechaCarga = hecho.fechaCarga().format(formatter);
    this.origen = hecho.origen().name();
    this.video = hecho.video();
    this.imagen = hecho.imagen();
    this.provincia=hecho.getProvincia().getNombreFormateado();
    this.fechaAcontecimientoSinFormatear = String.valueOf(hecho.fechaAcontecimiento());

    if(hecho.contribuyente()==null){
      this.contribuyente = "Anonimo";
    }else {
      this.contribuyente = hecho.contribuyente().getUsuario();
    }
  }

  public Long getId() {
    return id;
  }
  public String getTitulo() {
    return titulo;
  }
  public String getDescripcion() {
    return descripcion;
  }
  public String getCategoria() {
    return categoria;
  }
  public double getLatitud() {
    return latitud;
  }
  public double getLongitud() {
    return longitud;
  }
  public String getFechaAcontecimiento() {
    return fechaAcontecimiento;
  }
  public String getFechaCarga() {
    return fechaCarga;
  }
  public String getOrigen() {
    return origen;
  }
  public String getProvincia() {return provincia;}
  public String getVideo() {
    return video;
  }
  public String getImagen() { return imagen; }
  public String getContribuyente() {
    return this.contribuyente;
  }
  public String getFechaAcontecimientoSinFormatear() {return this.fechaAcontecimientoSinFormatear;}
}
