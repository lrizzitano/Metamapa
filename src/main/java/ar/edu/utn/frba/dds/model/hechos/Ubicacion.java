package ar.edu.utn.frba.dds.model.hechos;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Ubicacion {
  @Column
  public Double latitud;
  @Column
  public Double longitud;
  @Column
  @Enumerated(EnumType.STRING)
  public Provincia provincia;

  public Ubicacion(Double latitud, Double longitud, Provincia provincia, ServicioUbicador ubicador) {
    this.latitud = Objects.requireNonNull(latitud, "latitud no puede ser null");
    this.longitud = Objects.requireNonNull(longitud, "longitud no puede ser null");
    if (provincia == null) {
      if (ubicador != null) {
        this.provincia = ubicador.getProvincia(this.latitud, this.longitud);
      } else {
        this.provincia = Provincia.PROVINCIA_DESCONOCIDA;
      }
    } else {
      this.provincia = provincia;
    }
  }

  public Ubicacion() {}

  Double getLatitud() {
    return this.latitud;
  }

  Double getLongitud() {
    return this.longitud;
  }

  Provincia getProvincia() {
    return this.provincia;
  }
}
