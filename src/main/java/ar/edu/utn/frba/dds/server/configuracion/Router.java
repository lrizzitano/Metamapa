package ar.edu.utn.frba.dds.server.configuracion;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;

public class Router implements WithSimplePersistenceUnit {

  public void routearApp(Javalin app) {

    /*
    app.before(ctx -> entityManager().getTransaction().begin());

    app.after(ctx -> entityManager().getTransaction().commit());*/

    // Ruta raíz que renderiza una plantilla Handlebars
    app.get("/", ctx -> {
      Map<String, Object> model = new HashMap<>();
      model.put("mensaje", "Hola Mundo desde Handlebars!");
      ctx.render("templates/index", model);
    });

  }
}
