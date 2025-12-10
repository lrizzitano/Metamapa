package ar.edu.utn.frba.dds.controllers;
import ar.edu.utn.frba.dds.model.fuentes.Agregador;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.metamapa.FuenteMetaMapa;
import ar.edu.utn.frba.dds.model.repositorios.FuentesRepositoryJPA;
import ar.edu.utn.frba.dds.model.repositorios.RepoColecciones;
import ar.edu.utn.frba.dds.server.configuracion.Logger;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FuentesController implements WithSimplePersistenceUnit, TransactionalOps {
  public void subirFuente(Context ctx) {
    String tipo = ctx.formParam("tipoFuente");

    if (tipo == null || tipo.isBlank()) {
      new Logger().info("Faltan campos por completar para fuente: TIPO");
      ctx.result("Campos incompletos");
      return;
    }

    if (tipo.equals("metamapa")) {
      String url = ctx.formParam("url");

      if (url == null || url.isBlank()) {
        new Logger().info("Faltan campos por completar para fuente: URL");
        ctx.result("Campos incompletos");
        return;
      }

      Fuente fuente = new FuenteMetaMapa(url);

      withTransaction(() -> {
        new FuentesRepositoryJPA().persist(fuente);
      });
      ctx.header("HX-Redirect", "/panelDeControl/fuentes");
    } else if (tipo.equals("estatica")) {

      // falta tratar este caso
      ctx.header("HX-Redirect", "/panelDeControl/fuentes");
    }
  }

  public Map<String, Object> fuentes() {
    FuentesRepositoryJPA repoFuentes = new FuentesRepositoryJPA();
    Map<String, Object> model = new HashMap<>();

    withTransaction(() -> {
      Set<Fuente> fuentes = repoFuentes.obtenerFuentes()
          .stream()
          .filter(f -> !(f instanceof Agregador))
          .collect(Collectors.toSet());

      Set<FuenteDTO> fuentesDTO = fuentes.stream().map(FuenteDTO::new).collect(Collectors.toSet());
      model.put("fuentes", fuentesDTO);
    });

    return model;
  }
}
