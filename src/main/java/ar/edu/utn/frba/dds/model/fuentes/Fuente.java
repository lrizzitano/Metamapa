package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.hechos.Hecho;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;


@Entity
@Table(name = "fuentes")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Fuente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public Long getId(){
    return id;
  }

  public abstract Set<Hecho> obtenerHechos(Filtro filtro);

  public Set<Hecho>  obtenerHechos(String busqueda, Filtro filtro) {
    Set<Hecho> hechosFiltrado = this.obtenerHechos(filtro);
    return this.fullTextSearch(busqueda, hechosFiltrado);
  }

  protected Set<Hecho> fullTextSearch(String busqueda, Set<Hecho> hechos) {
    return hechos.stream().filter(h -> (h.titulo()+h.descripcion()).toLowerCase().contains(busqueda.toLowerCase()))
        .collect(Collectors.toSet());
  }

  public String detalle() { return "-"; }

  public abstract String getNombre();
}
