package ar.edu.utn.frba.dds.model.hechos;

public enum Provincia {
  PROVINCIA_DESCONOCIDA,
  CABA,
  BUENOS_AIRES,
  ENTRE_RIOS,
  SANTA_FE,
  CORRIENTES,
  MISIONES,
  FORMOSA,
  CHACO,
  SANTIAGO_DEL_ESTERO,
  TUCUMAN,
  SALTA,
  JUJUY,
  CATAMARCA,
  LA_RIOJA,
  SAN_JUAN,
  SAN_LUIS,
  MENDOZA,
  CORDOBA,
  LA_PAMPA,
  NEUQUEN,
  RIO_NEGRO,
  CHUBUT,
  SANTA_CRUZ,
  TIERRA_DEL_FUEGO;

  public static Provincia parseProvincia(String nombre) {
    if (nombre == null || nombre.isBlank())
      return Provincia.PROVINCIA_DESCONOCIDA;

    try {
      return Provincia.valueOf(
          nombre.trim()
              .toUpperCase()
              .replace(' ', '_')
      );
    } catch (IllegalArgumentException e) {
      return Provincia.PROVINCIA_DESCONOCIDA;
    }
  }
}
