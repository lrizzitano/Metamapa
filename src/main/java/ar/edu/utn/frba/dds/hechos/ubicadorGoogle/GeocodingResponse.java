package ar.edu.utn.frba.dds.hechos.ubicadorGoogle;

import java.util.List;

class GeocodingResponse {
  List<Result> results;
  String status;
}

class Result {
  String formatted_address;
}