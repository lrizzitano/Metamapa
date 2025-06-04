package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.execpciones.AccesoRecursoFallidoException;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsimidorDeApiHttp {

  public ConsimidorDeApiHttp() {

  }

  public Object enviarRequest(HttpClient cliente, HttpRequest request) {

    try {
      HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
      return validarStatusCode(response);

    } catch (IOException | InterruptedException e) {
      throw new AccesoRecursoFallidoException("Error al realizar la solicitud " + request.method()
          + " a la ruta " + request.uri(), e);
    }
  }

  private Object validarStatusCode(HttpResponse<String> response) {

    if (200 <= response.statusCode() && response.statusCode() < 300) {
      Gson gson = new Gson();
      return gson.fromJson(response.body(), Object.class);
    } else {
      throw new AccesoRecursoFallidoException("Acceso a recurso fallido con codigo: "
          + response.statusCode(), null);
    } // no se si sacarle o dejarle el segundo parametro a la excepcion
  }
}
