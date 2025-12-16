package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeCambio;

public class SolicitudDeCambioDTO {

  public Long id;
  public HechoDTO hechoActual;
  public HechoDTO hechoNuevo;

  public SolicitudDeCambioDTO(SolicitudDeCambio solicitudDeCambio) {
    this.hechoActual = new HechoDTO(solicitudDeCambio.getHechoParacambiar());
    this.hechoNuevo = new HechoDTO(solicitudDeCambio.getHechoModificado());
    this.id = solicitudDeCambio.getId();
  }

  public Long id(){
    return id;
  }
  public HechoDTO hechoActual(){
    return hechoActual;
  }

  public HechoDTO hechoNuevo(){
    return hechoNuevo;
  }
}
