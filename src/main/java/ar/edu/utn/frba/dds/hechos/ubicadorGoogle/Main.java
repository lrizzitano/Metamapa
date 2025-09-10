package ar.edu.utn.frba.dds.hechos.ubicadorGoogle;

import ar.edu.utn.frba.dds.hechos.ServicioUbicador;

public class Main {
  public static void main(String[] args) {
    ServicioUbicador ubicador = new UbicadorGoogle();
    ubicador.getProvincia(-43.3, -65.1);
  }

}