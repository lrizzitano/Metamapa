package ar.edu.utn.frba.dds.model.hechos.ubicadorGoogle;

import ar.edu.utn.frba.dds.model.hechos.Provincia;
import ar.edu.utn.frba.dds.model.hechos.ServicioUbicador;
import io.github.cdimascio.dotenv.Dotenv;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UbicadorGoogle implements ServicioUbicador {
  Dotenv dotenv = Dotenv.load();
  String apiKey = dotenv.get("GOOGLE_API_KEY"); // importante tener el .env con la key en el root del proyecto
  private static final Map<String, Provincia> mapa = new HashMap<>();

  static {
    for (Provincia p : Provincia.values()) {
      mapa.put(p.name(), p);
    }
  }

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
        String provincia = geo.results.get(0).formatted_address;

        // Normalizar: quitar tildes, pasar a mayúsculas
        String normalized = Normalizer.normalize(provincia, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")      // quita acentos
            .toUpperCase()
            .replace("PROVINCIA DE ", "")
            .replace("CDAD. AUTONOMA DE BUENOS AIRES", "CABA")
            .replace(", ARGENTINA", "")
            .trim()
            .replace(" ", "_"); // match con formato del enum

        System.out.println(normalized);
        return mapa.getOrDefault(normalized, Provincia.PROVINCIA_DESCONOCIDA);

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
