package ar.edu.utn.frba.dds;

import java.util.Set;
import java.util.function.Predicate;


public class Usuario {

  private String nombre;
  private String apellido;
  private int edad;
  private boolean esContribuyente = false;

  public Usuario(String nombre, String apellido, Integer edad) {
    this.nombre = nombre == null ? "Usuario" : nombre;
    this.apellido = apellido == null ? "Anonimo" : apellido;
    this.edad = edad == null ? 0 : edad;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public int getEdad() {
    return edad;
  }

  public boolean esContribuyente() {
    return esContribuyente;
  }

  public Set<Hecho> verHechosFiltrados(Coleccion unaColeccion, Predicate<Hecho> unFiltro) {
    return unaColeccion.hechos(unFiltro);
  }

  public Solicitud crearSolicitud(Hecho unHecho, String fundamentacion) {

    if (fundamentacion == null) {
      throw new SolicitudInvalidaException("La fundamentacion esta vacia");
    }
    if (unHecho == null) {
      throw new SolicitudInvalidaException("No se asigno ningu hecho");
    }


    return new Solicitud(unHecho, fundamentacion);
  }

  public Set<Hecho> verHechos(Coleccion unaColeccion) {
    return unaColeccion.hechos(hecho -> true);
  }

}
