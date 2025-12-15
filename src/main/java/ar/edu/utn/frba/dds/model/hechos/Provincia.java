package ar.edu.utn.frba.dds.model.hechos;

public enum Provincia {
PROVINCIA_DESCONOCIDA("Provincia desconocida"),
CABA("Ciudad Autónoma de Buenos Aires"),
BUENOS_AIRES("Buenos Aires"),
ENTRE_RIOS("Entre Ríos"),
SANTA_FE("Santa Fe"),
CORRIENTES("Corrientes"),
MISIONES("Misiones"),
FORMOSA("Formosa"),
CHACO("Chaco"),
SANTIAGO_DEL_ESTERO("Santiago del Estero"),
TUCUMAN("Tucumán"),
SALTA("Salta"),
JUJUY("Jujuy"),
CATAMARCA("Catamarca"),
LA_RIOJA("La Rioja"),
SAN_JUAN("San Juan"),
SAN_LUIS("San Luis"),
MENDOZA("Mendoza"),
CORDOBA("Córdoba"),
LA_PAMPA("La Pampa"),
NEUQUEN("Neuquén"),
RIO_NEGRO("Río Negro"),
CHUBUT("Chubut"),
SANTA_CRUZ("Santa Cruz"),
TIERRA_DEL_FUEGO("Tierra del Fuego");

private final String nombreFormateado;

Provincia(String nombreFormateado) {
  this.nombreFormateado = nombreFormateado;
}

public String getNombreFormateado() {
  return nombreFormateado;
}

  public static Provincia desdeNombre(String texto) {

    if (texto == null) {
      return PROVINCIA_DESCONOCIDA;
    }


    for (Provincia p : Provincia.values()) {

      if (p.nombreFormateado.equalsIgnoreCase(texto)) {
        return p;
      }
    }

    return PROVINCIA_DESCONOCIDA;
  }

@Override
public String toString() {
  return nombreFormateado;
}

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
