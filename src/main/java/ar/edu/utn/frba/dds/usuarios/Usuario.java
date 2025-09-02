package ar.edu.utn.frba.dds.usuarios;

import javax.persistence.*;

@Entity
@Table(name = "Usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoUsuario")
@DiscriminatorValue("usuario")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "apellido")
  private String apellido;
  @Column(name = "edad")
  private int edad;
  @Column(name = "esContribuyente")
  private boolean esContribuyente = false;

  public Usuario() {}
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
