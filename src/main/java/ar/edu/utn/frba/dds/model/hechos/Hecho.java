package ar.edu.utn.frba.dds.model.hechos;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    @Embedded
    private Ubicacion ubicacion;
    @Column(name = "fechaCarga")
    private LocalDateTime fechaCarga;
    @Column(name = "fechaAcontecimiento")
    private LocalDateTime fechaAcontecimiento;
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
        Ubicacion ubicacion,
        LocalDateTime fechaCarga,
        LocalDateTime fechaAcontecimiento,
        Origen origen) {
        this.id = id;
        this.titulo = Objects.requireNonNull(titulo, "titulo no puede ser null");
        this.descripcion = Objects.requireNonNull(descripcion, "descripcion no puede ser null");
        this.categoria = Objects.requireNonNull(categoria, "categoria no puede ser null");
        this.ubicacion = Objects.requireNonNull(ubicacion, "ubicacion no puede ser null");
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
    public Ubicacion ubicacion() {
        return ubicacion;
    }
    public Double latitud() {
        return ubicacion.getLatitud();
    }
    public Double longitud() {
        return ubicacion.getLongitud();
    }
    public LocalDateTime fechaCarga() {
        return fechaCarga;
    }
    public LocalDateTime fechaAcontecimiento() {
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

    public Provincia getProvincia() {
        return ubicacion.getProvincia();
    }

    Double getLatitud() {
        return this.ubicacion.latitud;
    }

    Double getLongitud() {
        return this.ubicacion.longitud;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("titulo", titulo);
        map.put("descripcion", descripcion);
        map.put("latitud", String.valueOf(ubicacion.getLatitud()));
        map.put("longitud", String.valueOf(ubicacion.getLongitud()));
        return map;
    }
}
