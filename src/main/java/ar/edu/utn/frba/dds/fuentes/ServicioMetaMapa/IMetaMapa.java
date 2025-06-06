package ar.edu.utn.frba.dds.fuentes.ServicioMetaMapa;

import ar.edu.utn.frba.dds.hechos.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudDeEliminacion;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.Map;
import java.util.Set;

public interface IMetaMapa {

  @GET("/hechos")
  Call<Set<Hecho>> obtenerHechos(@QueryMap Map<String, String> filtros);

  @GET("/colecciones/:{id}/hechos")
  Call<Set<Hecho>> obtenerHechosDeColeccion(@Path("id") String identificadorColeccion, @QueryMap Map<String, String> filtros);

  @POST("/solicitudes")
  Call<Void> enviarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud);
}
