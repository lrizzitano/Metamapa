package ar.edu.utn.frba.dds.Fuentes;

import java.net.URL;
import java.time.Instant;
import java.util.Map;

public interface Conexion {
  Map<String, Object> siguienteHecho(URL url, Instant fechaUltimaConsulta);

}
