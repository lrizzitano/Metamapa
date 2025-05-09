package ar.edu.utn.frba.dds;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Filtro {
  TITULO(valor ->
      contiene(Hecho::titulo, valor)),
  DESCRIPCION(valor ->
      contiene(Hecho::descripcion, valor)),
  CATEGORIA(valor ->
      contiene(Hecho::categoria, valor)),
  LATITUD_MAYOR(valor ->
      compararDouble(Hecho::latitud, valor, TipoComparacion.MAYOR)),
  LATITUD_MENOR(valor ->
      compararDouble(Hecho::latitud, valor, TipoComparacion.MENOR)),
  LONGITUD_MAYOR(valor ->
      compararDouble(Hecho::longitud, valor, TipoComparacion.MAYOR)),
  LONGITUD_MENOR(valor ->
      compararDouble(Hecho::longitud, valor, TipoComparacion.MENOR)),
  FECHA_CARGA_ANTES(valor ->
      compararFecha(Hecho::fechaCarga, valor, TipoComparacion.MENOR)),
  FECHA_CARGA_DESPUES(valor ->
      compararFecha(Hecho::fechaCarga, valor, TipoComparacion.MAYOR)),
  FECHA_HECHO_ANTES(valor ->
      compararFecha(Hecho::fechaAcontecimiento, valor, TipoComparacion.MENOR)),
  FECHA_HECHO_DESPUES(valor ->
      compararFecha(Hecho::fechaAcontecimiento, valor, TipoComparacion.MAYOR));
  //ORIGEN_ES(valor -> contiene(Hecho::origen, valor)),
  //ORIGEN_NO_ES(valor -> hecho -> !hecho.origen().toLowerCase().contains(valor.toLowerCase()));

  private final Function<String, Predicate<Hecho>> funcionCreadora;

  Filtro(Function<String, Predicate<Hecho>> funcionCreadora) {
    this.funcionCreadora = funcionCreadora;
  }

  public Predicate<Hecho> crearFiltro(String valor) {
    return funcionCreadora.apply(valor);
  }

  //Auxiliares
  private enum TipoComparacion {
    MAYOR, MENOR
  }

  private static Predicate<Hecho> contiene(Function<Hecho, String> getter, String valor) {
    String lowered = valor.toLowerCase();
    return hecho -> getter.apply(hecho).toLowerCase().contains(lowered);
  }

  private static Predicate<Hecho> compararDouble(Function<Hecho, Double> getter, String valor,
                                                 TipoComparacion tipoComparacion) {
    return hecho -> tipoComparacion == TipoComparacion.MAYOR
        ? getter.apply(hecho) >= Double.parseDouble(valor)
        : getter.apply(hecho) <= Double.parseDouble(valor);
  }

  private static Predicate<Hecho> compararFecha(Function<Hecho, LocalDate> getter, String valor,
                                                TipoComparacion tipoComparacion) {
    return hecho -> getter.apply(hecho).equals(LocalDate.parse(valor))
        || tipoComparacion == TipoComparacion.MAYOR
        ? getter.apply(hecho).isAfter(LocalDate.parse(valor))
        : getter.apply(hecho).isBefore(LocalDate.parse(valor));
  }
}