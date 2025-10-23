package ar.edu.utn.frba.dds.server.configuracion;
import io.javalin.Javalin;
import java.util.Map;
import java.util.HashMap;

public class Routes {

  public static void routearApp(Javalin app) {

    // Ruta raíz que renderiza una plantilla Handlebars
    app.get("/", ctx -> {
      Map<String, Object> model = new HashMap<>();
      model.put("mensaje", "Hola Mundo desde Handlebars!");
      ctx.render("templates/index", model);
    });

  }
}
