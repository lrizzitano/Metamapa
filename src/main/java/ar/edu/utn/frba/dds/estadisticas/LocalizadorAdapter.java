package ar.edu.utn.frba.dds.estadisticas;

public interface LocalizadorAdapter {
  Provincia localizarProvincia(Long latitud, Long longitud);
}
