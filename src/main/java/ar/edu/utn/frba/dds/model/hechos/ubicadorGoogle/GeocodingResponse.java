package ar.edu.utn.frba.dds.model.hechos.ubicadorGoogle;

import java.util.List;

public class GeocodingResponse {
  List<Result> results;
  String status;
}

class Result {
  String formatted_address;
}