package ar.edu.utn.frba.dds.model.fuentes.metamapa;

import ar.edu.utn.frba.dds.model.hechos.Hecho;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudDeEliminacion;
import java.util.Map;
import java.util.Set;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IMetaMapa {

  @GET("/api/hechos")
  Call<Set<Hecho>> obtenerHechos(@QueryMap Map<String, String> filtros);

  @GET("/api/colecciones/{id}/hechos")
  Call<Set<Hecho>> obtenerHechosDeColeccion(@Path("id") String identificadorColeccion, @QueryMap Map<String, String> filtros);

  @POST("/api/solicitudes")
  Call<Void> enviarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud);
}
