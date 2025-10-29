package ar.edu.utn.frba.dds.model.hechos;

public interface ServicioUbicador {
  Provincia getProvincia(Double latitud, Double longitud);
}
