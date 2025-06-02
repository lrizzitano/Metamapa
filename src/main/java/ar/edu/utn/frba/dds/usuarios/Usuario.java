package ar.edu.utn.frba.dds.usuarios;


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

  public void contribuir() {
    this.esContribuyente = true;
  }

  public boolean estaRegistrado(){
    return (this.edad != 0 || !this.nombre.equals("Usuario") || !this.apellido.equals("Anonimo"));
  }

  /*PODRIA IR AQUI, EN LUGAR DE EN LA FUENTE DINAMICA, no expongo contribuir() a ser llamado libremente
  public void agregarHecho(Hecho hecho)
  {
    this.esContribuyente = true;
    FuenteDinamica.instance().agregarHecho(hecho);
  }
   */
}
