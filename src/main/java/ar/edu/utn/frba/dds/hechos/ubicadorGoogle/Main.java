package ar.edu.utn.frba.dds.hechos.ubicadorGoogle;

import ar.edu.utn.frba.dds.hechos.ServicioUbicador;

public class Main {
  public static void main(String[] args) {
    ServicioUbicador ubicador = new UbicadorGoogle();
    System.out.println(ubicador.getProvincia(-31.4166666666666667, -64.18333333333));
  }

}