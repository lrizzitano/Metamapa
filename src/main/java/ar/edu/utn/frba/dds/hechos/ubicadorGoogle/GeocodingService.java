package ar.edu.utn.frba.dds.hechos.ubicadorGoogle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingService {
  @GET("geocode/json")
  Call<GeocodingResponse> inverseGeocode(
      @Query("latlng") String latlng,
      @Query("key") String apiKey,
      @Query("result_type") String resultType,
      @Query("language") String language
  );
}