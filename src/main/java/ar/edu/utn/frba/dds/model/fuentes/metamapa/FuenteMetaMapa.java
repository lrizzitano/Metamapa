package ar.edu.utn.frba.dds.model.fuentes.metamapa;

import ar.edu.utn.frba.dds.model.execpciones.AccesoRecursoFallidoException;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Path;
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

  @Transient
  private  Retrofit retrofit;

  public FuenteMetaMapa(String urlAPI) {
    this.urlAPI = urlAPI;

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
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

    if (response.code() == 204 || response.body() == null) {
      return new HashSet<>();
    }

    return response.body();
  }

  public Set<Hecho> obtenerHechosDeColeccion(Filtro filtro, String identificadorColeccion) {
    IMetaMapa metaMapa = retrofit.create(IMetaMapa.class);
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
    IMetaMapa metaMapa = retrofit.create(IMetaMapa.class);
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
}