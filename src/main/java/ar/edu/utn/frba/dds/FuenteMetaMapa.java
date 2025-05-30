package ar.edu.utn.frba.dds;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Set;
import java.util.function.Predicate;

public class FuenteMetaMapa implements Fuente {

  String pathInicial;
  HttpClient cliente;
  ConsimidorDeAPIHttp conexionMetaMapa;

  void FunteMetaMapa(String pathIncial) {
    this.pathInicial = pathIncial;
    cliente = HttpClient.newHttpClient();
    conexionMetaMapa = new ConsimidorDeAPIHttp();
  }

  @Override
  public Set<Hecho> obtenerHechos(Predicate<Hecho> filtro) {

    HttpRequest request = HttpRequest.newBuilder() // Estaria bueno definir un timeout
                                     .uri(URI.create(pathInicial + "/hechos" + this.filtroAQueryParameter(filtro))) // como pasamos los filtros a query?
                                     .header("Accept", "application/json")       // Recibo json
                                     .GET()
                                     .build();

    return (Set<Hecho>) conexionMetaMapa.enviarRequest(cliente, request);
  }

  public Set<Hecho> obtenerHechosDeColeccion(Predicate<Hecho> filtro, String identificadorColeccion) {

    HttpRequest request = HttpRequest.newBuilder() // Estaria bueno definir un timeout
        .uri(URI.create(pathInicial + "/hechos/:" + identificadorColeccion + "/" + this.filtroAQueryParameter(filtro))) // como pasamos los filtros a query?
        .header("Content-Type", "application/json") // Envio json en el body
        .header("Accept", "application/json")       // Recibo json
        .GET()
        .build();

    return (Set<Hecho>) conexionMetaMapa.enviarRequest(cliente, request);
  }

  public void enviarSolicitudDeEliminacion() {

    // Recive por parametro el objeto solicitud, o recibe por parametro los atributos y la instanciamos aca?
    Gson gson = new Gson();

    HttpRequest request = HttpRequest.newBuilder() // Estaria bueno definir un timeout
        .uri(URI.create(pathInicial + "/solicitudes"))// como pasamos los filtros a query?
        .header("Content-Type", "application/json") // Envio json en el body
        .header("Accept", "application/json")       // Recibo json
        .POST(HttpRequest.BodyPublishers.ofString(gson.toString()))
        .build();

    conexionMetaMapa.enviarRequest(cliente, request);
  }
}
