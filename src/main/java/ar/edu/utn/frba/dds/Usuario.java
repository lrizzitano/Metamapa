package ar.edu.utn.frba.dds;
import java.util.Set;


public class Usuario {

  private String nombre;
  private String apellido;
  private int edad;
  private boolean esContribuyente;

   public Set<Hecho> verHechosFiltrados(Coleccion unaColeccion, Filtro unFiltro)
  {
    return unaColeccion.hechos(unFiltro);
  }

  public void crearSolicitud(Hecho unHecho,String fundamentacion){

    if(fundamentacion==null){throw new SolicitudInvalidaException("La fundamentacion esta vacia");}
    if(unHecho==null){throw new SolicitudInvalidaException("No se asigno ningu hecho");}

     new Solicitud(unHecho,fundamentacion);
  }

  public Set<Hecho> verHechos(Coleccion unaColeccion)
  {
    return unaColeccion.hechos(Hecho -> true);
  }

  void contribur(Hecho unHecho) {

    /*busca en las collecion, se agrega a las que tengan el mismo criterioDePertenencia=Categoria

    colecciones.stream()
        .filter(unaColecCion -> unaColecCion.getCriterio().equals(unHecho.getCategoria()))
        .forEach(unaColeccion -> unaColeccion.agregarHecho(unHecho));
        */
  }


}
