package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.server.exceptions.ErrorRenderizadoException;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.rendering.FileRenderer;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import com.github.jknack.handlebars.Options;

public class HandlebarsRender implements FileRenderer {

  private final Handlebars handlebars;

  public HandlebarsRender() {
    //para dev, lee en .hbs de disco siempre, sirve para ir cambiándolo sin reiniciar
    FileTemplateLoader loader = new FileTemplateLoader("src/main/resources", ".hbs");

    this.handlebars = new Handlebars(loader);
    this.handlebars.registerHelper("json", (context, options)
        -> new Gson().toJson(context));

    this.handlebars.registerHelper("esIgual", new Helper<Object>() {
      @Override
      public CharSequence apply(Object context, Options options) throws java.io.IOException {
        Object parametroComparacion = options.param(0);

        if (context != null && context.equals(parametroComparacion)) {
          return options.fn();
        }
        else{
          return options.inverse();
        }
      }
    });
  }

  @NotNull
  @Override
  public String render(@NotNull String filePath, @NotNull Map<String, ?> model, @NotNull Context ctx) {
    try {
      Template template = handlebars.compile(filePath);
      return template.apply(model);
    } catch (Exception e) {
      throw new ErrorRenderizadoException("Error rendering template: " + filePath, e);
    }
  }

}
