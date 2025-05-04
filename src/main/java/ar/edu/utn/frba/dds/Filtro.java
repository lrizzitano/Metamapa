package ar.edu.utn.frba.dds;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Filtro {
  TITULO(valor -> contiene(Hecho::titulo, valor)),
  DESCRIPCION(valor -> contiene(Hecho::descripcion, valor)),
  CATEOGIRA(valor -> contiene(Hecho::categoria, valor)),
  LATITUD_MAYOR(valor -> compararDouble(Hecho::latitud, valor, Comparar.MAYOR)),
  LATITUD_MENOR(valor -> compararDouble(Hecho::latitud, valor, Comparar.MENOR)),
  LONGITUD_MAYOR(valor -> compararDouble(Hecho::longitud, valor, Comparar.MAYOR)),
  LONGITUD_MENOR(valor -> compararDouble(Hecho::longitud, valor, Comparar.MENOR)),
  FECHA_CARGA_ANTES(valor -> compararFecha(Hecho::fechaCarga, valor, Comparar.MENOR)),
  FECHA_CARGA_DESPUES(valor -> compararFecha(Hecho::fechaCarga, valor, Comparar.MAYOR)),
  FECHA_HECHO_ANTES(valor -> compararFecha(Hecho::fechaAcontecimiento, valor, Comparar.MENOR)),
  FECHA_HECHO_DESPUES(valor -> compararFecha(Hecho::fechaAcontecimiento, valor, Comparar.MAYOR));
  //ORIGEN_ES(valor -> contiene(Hecho::origen, valor)),
  //ORIGEN_NO_ES(valor -> hecho -> !hecho.origen().toLowerCase().contains(valor.toLowerCase()));

  private final Function<String, Predicate<Hecho>> constructor;

  Filtro(Function<String, Predicate<Hecho>> constructor) {
    this.constructor = constructor;
  }

  public Predicate<Hecho> crearFiltro(String valor) {
    return constructor.apply(valor);
  }

  //Auxiliares
  private enum Comparar { MAYOR, MENOR }

  private static Predicate<Hecho> contiene(Function<Hecho, String> getter, String valor) {
    String lowered = valor.toLowerCase();
    return hecho -> getter.apply(hecho).toLowerCase().contains(lowered);
  }

  private static Predicate<Hecho> compararDouble(Function<Hecho, Double> getter, String valor,
                                                 Comparar cmp) {
    return hecho -> cmp == Comparar.MAYOR
        ? getter.apply(hecho) >= Double.parseDouble(valor)
        : getter.apply(hecho) <= Double.parseDouble(valor);
  }

  private static Predicate<Hecho> compararFecha(Function<Hecho, LocalDate> getter, String valor,
                                                Comparar cmp) {
    return hecho -> cmp == Comparar.MAYOR
        ? getter.apply(hecho).isAfter(LocalDate.parse(valor))
        : getter.apply(hecho).isBefore(LocalDate.parse(valor));
  }
}