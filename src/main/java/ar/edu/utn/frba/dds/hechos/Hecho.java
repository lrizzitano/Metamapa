package ar.edu.utn.frba.dds.hechos;

import ar.edu.utn.frba.dds.usuarios.Usuario;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Hecho")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hecho_id")
    private Long id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "latitud")
    private Double latitud;
    @Column(name = "longitud")
    private Double longitud;
    @Column(name = "fechaCarga")
    private LocalDate fechaCarga;
    @Column(name = "fechaAcontecimiento")
    private LocalDate fechaAcontecimiento;
    @Enumerated(EnumType.STRING)
    @Column(name = "origen")
    private Origen origen;
    @Column(name = "multimedia")
    private String multimedia;
    @ManyToOne()
    @JoinColumn(name = "contribuyente_id")
    private Usuario contribuyente;
    @Column(name = "fueRevisado")
    private Boolean fueRevisado;

    public Hecho() {}
    public Hecho(
        Long id,
        String titulo,
        String descripcion,
        String categoria,
        Double latitud,
        Double longitud,
        LocalDate fechaCarga,
        LocalDate fechaAcontecimiento,
        Origen origen) {
      this.id = id;
      this.titulo = Objects.requireNonNull(titulo, "titulo no puede ser null");
      this.descripcion = Objects.requireNonNull(descripcion, "descripcion no puede ser null");
      this.categoria = Objects.requireNonNull(categoria, "categoria no puede ser null");
      this.latitud = Objects.requireNonNull(latitud, "latitud no puede ser null");
      this.longitud = Objects.requireNonNull(longitud, "longitud no puede ser null");
      this.fechaCarga = Objects.requireNonNull(fechaCarga, "fechaCarga no puede ser null");
      this.fechaAcontecimiento = Objects.requireNonNull(fechaAcontecimiento, "fechaAcontecimiento no puede ser null");
      this.origen = Objects.requireNonNull(origen, "origen no puede ser null");
      this.multimedia = null;
      this.contribuyente = null;
      this.fueRevisado = false;
    }

    public Long id() {
        return id;
    }
    public String titulo() {
        return titulo;
    }
    public String descripcion() {
        return descripcion;
    }
    public String categoria() {
        return categoria;
    }
    public Double latitud() {
        return latitud;
    }
    public Double longitud() {
        return longitud;
    }
    public LocalDate fechaCarga() {
        return fechaCarga;
    }
    public LocalDate fechaAcontecimiento() {
        return fechaAcontecimiento;
    }
    public Origen origen() {
        return origen;
    }
    public String multimedia() {
        return multimedia;
    }
    public Usuario contribuyente() {
        return contribuyente;
    }
    public Boolean fueRevisado() {
        return fueRevisado;
    }

    public void setContribuyente(Usuario contribuyente) {
        this.contribuyente = contribuyente;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public void setId(Long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof  Hecho)) {
            return false;
        }

        return this.titulo.equals(((Hecho) obj).titulo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }
}
