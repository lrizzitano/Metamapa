package ar.edu.utn.frba.dds;

import java.util.Set;

public class Administrador extends Usuario {

  public Coleccion crearColeccion(String titulo, String descripcion, Filtro criterioDePertenencia, Fuente fuente) {

    return new Coleccion(titulo, descripcion, criterioDePertenencia, fuente);
  }

  public Set<Solicitud> verSolicitudes() {
    return Solicitudes.instance().getPendientes();
  }

  public FuenteEstatica crearFuenteEstatica(String pathArchivo) {
    return new FuenteEstatica(pathArchivo);
  }

  public void aceptarSolicitud(Solicitud solicitud) {
    solicitud.aceptar(this);
  }

  public void rechazarSolicitud(Solicitud solicitud) {
    solicitud.rechazar(this);
  }
}
