package ar.edu.utn.frba.dds.model.fuentes.metamapa;

import ar.edu.utn.frba.dds.model.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.NullFiltro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Entity
@DiscriminatorValue("meta_mapa")
public class FuenteMetaMapa extends Fuente {

  public FuenteMetaMapa(){}

  @Column(name = "url_api_metamapa")
  private  String urlAPI;

  public FuenteMetaMapa(String urlAPI) {
    this.urlAPI = urlAPI;
  }

  // inicializacion lazy del cliente
  // se crea para cada request, medio tosco, pero es la unica manera pq si lo tenes como atributo
  //  transient lo perdes al ir a la base de datos y volver (vuelve y es null)
  // => lo configuras cada vez que lo usas y fue
  Retrofit getClienteRetroFit(String url) {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();

    return new Retrofit.Builder()
        .baseUrl(urlAPI)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  @Override
    public Set<Hecho> obtenerHechos(Filtro filtro) {
    new Logger().info(">>> Iniciando obtenerHechos");

    IMetaMapa metaMapa = this.getClienteRetroFit(urlAPI).create(IMetaMapa.class);
    new Logger().info(">>> IMetaMapa creado");

    Call<Set<Hecho>> request = metaMapa.obtenerHechos(filtro.toQueryParam());
    new Logger().info(">>> Request creado: " + request.request().url());

    Response<Set<Hecho>> response;
    try {
      new Logger().info(">>> Ejecutando request.execute()...");
      response = request.execute();
      new Logger().info(">>> Request ejecutado - Código: " + response.code());
    } catch (IOException e) {
      new Logger().info(">>> Error en execute: " + e.getMessage());
      new Logger().loggearExcepcion(
          new AccesoRecursoFallidoException("Error al obtener hechos de la API: " + this.urlAPI)
      );
      return new HashSet<>();
    }

    evaluarCodigoHTTP(response);

    if (response.code() == 204 || response.body() == null) {
      return new HashSet<>();
    }

    return response.body();
  }

  public Set<Hecho> obtenerHechosDeColeccion(Filtro filtro, String identificadorColeccion) {
    IMetaMapa metaMapa = this.getClienteRetroFit(urlAPI).create(IMetaMapa.class);
    Call<Set<Hecho>> request = metaMapa.obtenerHechosDeColeccion(identificadorColeccion,
        filtro.toQueryParam());

    Response<Set<Hecho>> response;

    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AccesoRecursoFallidoException("Error al obtener hechos de la coleccion \""
          + identificadorColeccion + "\" de la API: " + this.urlAPI);
    }

    evaluarCodigoHTTP(response);
    if (response.code() == 204 || response.body() == null) {
      return new HashSet<>();
    }

    return response.body();
  }

  public void enviarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
    IMetaMapa metaMapa = this.getClienteRetroFit(urlAPI).create(IMetaMapa.class);
    Call<Void> request = metaMapa.enviarSolicitudDeEliminacion(solicitud);

    Response<Void> response;

    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AccesoRecursoFallidoException("Error al enviar una solicitud "
          + "de eliminacion a la API: " + this.urlAPI);
    }

    evaluarCodigoHTTP(response);


  }

  public String getUrlAPI() {
    return urlAPI;
  }

  private <T> void  evaluarCodigoHTTP(Response<T> response){
    if (response.code() == 204) {
      return; // no hay body, lo manejamos afuera
    }

    if (!response.isSuccessful()) {
      throw new AccesoRecursoFallidoException("Respuesta no exitosa " + this.urlAPI);
    }
  }

  @Override
  public String getNombre(){
    return "MetaMapa";
  }

  @Override
  public String detalle() {
   return "Url de origen: " + this.urlAPI;
  }
}