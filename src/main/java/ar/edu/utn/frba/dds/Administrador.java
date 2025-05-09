package ar.edu.utn.frba.dds;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;

public class Administrador extends Usuario {

  public Administrador(String nombre, String apellido, int edad) {
    super(requireNonNull(nombre), requireNonNull(apellido), edad);
  }

  public Coleccion crearColeccion(String titulo, String descripcion, Predicate<Hecho>
      criterioDePertenencia, Fuente fuente) {

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
