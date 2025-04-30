package ar.edu.utn.frba.dds;

public class Solicitud {
  private final String titulo;
  private final String fundamento;
  private final Solicitudes solicitudes = Solicitudes.instance();
  private Administrador responsable;

  public Solicitud(String titulo, String fundamento) {
    this.titulo = titulo;
    this.fundamento = fundamento;
    solicitudes.nuevaSolicitud(this);
  }

  public String getTitulo() {
    return titulo;
  }

  public String getFundamento() {
    return fundamento;
  }

  public Administrador getResponsable(){
    return responsable;
  }

  public void aceptar(Administrador admin) {
    if(this.responsable!=null)
        return;
    this.responsable = admin;
    solicitudes.aceptarSolicitud(this);
  }

  public void rechazar(Administrador admin) {
    if(this.responsable!=null)
      return;
    this.responsable = admin;
    solicitudes.rechazarSolicitud(this);
  }

}
