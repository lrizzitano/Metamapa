package ar.edu.utn.frba.dds.hechos.ubicadorGoogle;

import io.github.cdimascio.dotenv.Dotenv;
import ar.edu.utn.frba.dds.hechos.Provincia;
import ar.edu.utn.frba.dds.hechos.ServicioUbicador;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.util.Locale;

public class UbicadorGoogle implements ServicioUbicador {
  Dotenv dotenv = Dotenv.load();
  String apiKey = dotenv.get("GOOGLE_API_KEY"); // importante tener el .env con la key en el root del proyecto

  @Override
  public Provincia getProvincia(Double lat, Double lng) {
    String latlng = String.format(Locale.US,"%f,%f", lat, lng);
    Retrofit retrofit = RetrofitClient.getClient();
    GeocodingService service = retrofit.create(GeocodingService.class);

    try {
      Call<GeocodingResponse> call = service.inverseGeocode(latlng, apiKey, "administrative_area_level_1", "es");
      Response<GeocodingResponse> response = call.execute();

      if (response.isSuccessful() && response.body() != null) {
        GeocodingResponse geo = response.body();
        System.out.println("Status: " + geo.status);
        System.out.println("Provincia: " + geo.results.get(0).formatted_address);
      } else {
        assert response.errorBody() != null;
        System.out.println("Error: " + response.errorBody().string());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Provincia.PROVINCIA_DESCONOCIDA;
  }
}
