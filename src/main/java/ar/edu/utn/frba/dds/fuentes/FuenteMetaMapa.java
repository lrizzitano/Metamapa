package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.Solicitud;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Set;

public class FuenteMetaMapa implements Fuente {

  String pathInicial;
  HttpClient cliente;
  ConsimidorDeAPIHttp conexionMetaMapa;

  public FuenteMetaMapa(String pathIncial) {
    this.pathInicial = pathIncial;
    cliente = HttpClient.newHttpClient();
    conexionMetaMapa = new ConsimidorDeAPIHttp();
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    HttpRequest request = HttpRequest.newBuilder() // Estaria bueno definir un timeout
                                     .uri(URI.create(pathInicial + "/hechos" + filtro.toHttp())) // como pasamos los filtros a query?
                                     .header("Accept", "application/json")       // Recibo json
                                     .GET()
                                     .build();

    return (Set<Hecho>) conexionMetaMapa.enviarRequest(cliente, request);
  }

  public Set<Hecho> obtenerHechosDeColeccion(Filtro filtro, String identificadorColeccion) {

    HttpRequest request = HttpRequest.newBuilder() // Estaria bueno definir un timeout
        .uri(URI.create(pathInicial + "/hechos/:" + identificadorColeccion + "/" + filtro.toHttp())) // como pasamos los filtros a query?
        .header("Accept", "application/json") // Recibo json
        .GET()
        .build();

    return (Set<Hecho>) conexionMetaMapa.enviarRequest(cliente, request);
  }

  public void enviarSolicitudDeEliminacion(Solicitud solicitud) {

    // Recive por parametro el objeto solicitud, o recibe por parametro los atributos y la instanciamos aca?
    Gson gson = new Gson();

    HttpRequest request = HttpRequest.newBuilder() // Estaria bueno definir un timeout
        .uri(URI.create(pathInicial + "/solicitudes"))// como pasamos los filtros a query?
        .header("Content-Type", "application/json") // Envio json en el body
        .header("Accept", "application/json")       // Recibo json
        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(solicitud)))
        .build();

    conexionMetaMapa.enviarRequest(cliente, request);
  }
}
