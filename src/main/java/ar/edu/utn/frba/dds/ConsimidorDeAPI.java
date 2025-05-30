package ar.edu.utn.frba.dds;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ConsimidorDeAPI {

  public ConsimidorDeAPI() {}

  public Object enviarRequest(HttpClient cliente, HttpRequest request) {

    try {
      HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString()); // sincronica o asincronica?
      return validarStatusCode(response);

    } catch (IOException | InterruptedException e) {
      throw new AccesoARecursoFallidoException("Error al realizar la solicitud " + request.method() + " a la ruta " + request.uri(), e);
    }
  }

  public Object enviarRequestAsincronica(HttpClient cliente, HttpRequest request) {

    CompletableFuture<HttpResponse<String>> response = cliente.sendAsync(request, HttpResponse.BodyHandlers.ofString()); // sincronica o asincronica?
    // Completar
    return validarStatusCode(response);
  }

  public Object validarStatusCode(HttpResponse<String> response) {

    if (200 <= response.statusCode() && response.statusCode() < 300) {
      Gson gson = new Gson();
      return gson.fromJson(response.body(), Object.class);
    } else {
      throw new AccesoARecursoFallidoException("Acceso a recurso fallido con codigo: " + response.statusCode(), null); // no se si sacarle o dejarle el segundo parametro a la excepcion
    }
  }
}
