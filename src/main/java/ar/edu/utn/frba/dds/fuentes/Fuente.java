package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.hechos.Hecho;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import java.util.Set;
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

  public abstract Set<Hecho> obtenerHechos(Filtro filtro);

  public Set<Hecho>  obtenerHechos(String busqueda, Filtro filtro) {
    Set<Hecho> hechosFiltrado = this.obtenerHechos(filtro);
    return fullTextSearch(busqueda, hechosFiltrado);
  }

  private Set<Hecho> fullTextSearch(String busqueda, Set<Hecho> hechos) {

    Map<Integer, Hecho> docToHecho = new HashMap<>();
    int documentoId = 0;

    IndexWriter writer = null;
    Directory memoryIndex = new RAMDirectory();

    try {

      writer = new IndexWriter(memoryIndex, new IndexWriterConfig(new StandardAnalyzer()));

      for (Hecho hecho : hechos) {
        Document document = new Document();
        document.add(new TextField("titulo", hecho.titulo(), Field.Store.YES));
        document.add(new TextField("descripcion", hecho.descripcion(), Field.Store.YES));
        writer.addDocument(document);
        docToHecho.put(documentoId++, hecho);
      }

      writer.close();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    try (IndexReader reader = DirectoryReader.open(memoryIndex)) {

      IndexSearcher searcher = new IndexSearcher(reader);

      Query query = MultiFieldQueryParser.parse(
          new String[]{busqueda, busqueda},
          new String[]{"titulo", "descripcion"},
          new StandardAnalyzer()
      );

      TopDocs topDocs = searcher.search(query, 10);

      Set<Hecho> resultados = new LinkedHashSet<>();

      for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        resultados.add(docToHecho.get(scoreDoc.doc));
      }
      return resultados;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
