package ar.edu.utn.frba.dds.Usuarios;


import ar.edu.utn.frba.dds.Fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Solicitudes.SolicitudDeCambio;

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

  //Podria ir en la fuente pero CREO QUE HAY razones para que este aca
  public void agregarHecho(Hecho hecho)
  {
    //hecho.setContribuyente(this); ESTO DEBE FUNCIONAR, O SE ASUME QUE SE LLAMA CON UN HECHO CON ESE USUARIO
    FuenteDinamica.instance().agregarHecho(hecho);
    this.esContribuyente = true;
  }

}
