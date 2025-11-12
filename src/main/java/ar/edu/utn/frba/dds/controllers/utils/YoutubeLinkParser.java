package ar.edu.utn.frba.dds.controllers.utils;

public class YoutubeLinkParser {

  public static String obtenerIdVideo(String enlace) {
    if (enlace == null || enlace.isEmpty()) return null;
    enlace = enlace.trim();

    if (enlace.contains("v=")) {
      String parte = enlace.substring(enlace.indexOf("v=") + 2);
      return parte.split("&")[0];
    }

    String[] prefijos = {"youtu.be/", "/embed/", "/v/", "/shorts/"};
    for (String p : prefijos) {
      int i = enlace.indexOf(p);
      if (i != -1) {
        String parte = enlace.substring(i + p.length());
        return parte.split("[?&#]")[0];
      }
    }

    return null;
  }

  public static String obtenerVideoEmbebible(String enlace) {
    return "https://www.youtube.com/embed/".concat(obtenerIdVideo(enlace));
  }
}