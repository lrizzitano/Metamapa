package ar.edu.utn.frba.dds.model.hechos.ubicadorGoogle;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

  public static Retrofit getClient() {
    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }
}
