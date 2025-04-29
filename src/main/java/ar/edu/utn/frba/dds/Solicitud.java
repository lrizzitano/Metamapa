package ar.edu.utn.frba.dds;

public class Solicitud {
  private final String titulo;
  private final String fundamento;
  private final Solicitudes solicitudes = Solicitudes.instance();

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

  public void aceptar() {
    solicitudes.aceptarSolicitud(this);
  }

  public void rechazar() {
    solicitudes.rechazarSolicitud(this);
  }

}
