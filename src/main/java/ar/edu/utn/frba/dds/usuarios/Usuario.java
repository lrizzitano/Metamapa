package ar.edu.utn.frba.dds.usuarios;


public class Usuario {

  private String nombre;
  private String apellido;
  private int edad;
  private boolean esContribuyente = false;

  public Usuario(String nombre, String apellido, Integer edad) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
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

  public void contribuir() {
    this.esContribuyente = true;
  }

  public boolean estaRegistrado() {
    return nombre != null;
  }
}
