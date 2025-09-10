package ar.edu.utn.frba.dds.hechos;

public interface ServicioUbicador {
  Provincia getProvincia(Double latitud, Double longitud);
}
