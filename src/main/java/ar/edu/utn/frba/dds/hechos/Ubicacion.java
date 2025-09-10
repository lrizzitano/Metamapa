package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.estadisticas.Provincia;

public class Ubicacion {

  private Double latitud;
  private Double longitud;
  private Provincia provincia;

  public Ubicacion(Double latitud, Double longitud, Provincia provincia) {
    this.latitud = latitud;
    this.longitud = longitud;
    this.provincia = provincia;
  }

  public Double getLongitud() {
    return longitud;
  }

  public Double getLatitud() {
    return latitud;
  }

  public Provincia getProvincia() {
    return provincia;
  }
}
