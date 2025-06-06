package ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa;

import ar.edu.utn.frba.dds.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Set;

public class ServicioMetaMapa implements Fuente {
  private final String urlAPI;
  private final Retrofit retrofit;

  public ServicioMetaMapa(String urlAPI) {
    this.urlAPI = urlAPI;

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Path.class, new PathAdapter())
        .create();

    this.retrofit = new Retrofit.Builder()
                                .baseUrl(urlAPI)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
  }

  @Override
  public Set<Hecho> obtenerHechos(Filtro filtro) {
    IMetaMapa metaMapa = retrofit.create(IMetaMapa.class);
    Call<Set<Hecho>> request = metaMapa.obtenerHechos(filtro.toQueryParam());

    Response<Set<Hecho>> response;

    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AccesoRecursoFallidoException("Error al obtener hechos de la API: " + this.urlAPI);
    }

    evaluarCodigoHTTP(response);

    return response.body();
  }

  public Set<Hecho> obtenerHechosDeColeccion(Filtro filtro, String identificadorColeccion) {
    IMetaMapa metaMapa = retrofit.create(IMetaMapa.class);
    Call<Set<Hecho>> request = metaMapa.obtenerHechosDeColeccion(identificadorColeccion, filtro.toQueryParam());

    Response<Set<Hecho>> response;

    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AccesoRecursoFallidoException("Error al obtener hechos de la coleccion \"" + identificadorColeccion + "\" de la API: " + this.urlAPI);
    }

    evaluarCodigoHTTP(response);

    return response.body();
  }

  public void enviarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
    IMetaMapa metaMapa = retrofit.create(IMetaMapa.class);
    Call<Void> request = metaMapa.enviarSolicitudDeEliminacion(solicitud);

    Response<Void> response;

    try {
      response = request.execute();
    } catch (IOException e) {
      throw new AccesoRecursoFallidoException("Error al enviar una solicitud de eliminacion a la API: " + this.urlAPI);
    }

    evaluarCodigoHTTP(response);
  }

  public String getUrlAPI() {
    return urlAPI;
  }

  private <T> void  evaluarCodigoHTTP(Response<T> response){
    if (!response.isSuccessful() || response.body() == null) {
      throw new AccesoRecursoFallidoException("Respuesta no exitosa o vacía de la API: " + this.urlAPI);
    }
  }
}