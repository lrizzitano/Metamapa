package ar.edu.utn.frba.dds.Usuarios;


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

}
