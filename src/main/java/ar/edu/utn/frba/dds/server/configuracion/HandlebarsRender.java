package ar.edu.utn.frba.dds.server.configuracion;

import ar.edu.utn.frba.dds.server.exceptions.ErrorRenderizadoException;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.rendering.FileRenderer;


@FunctionalInterface
public interface HandlebarsRender extends FileRenderer {

  FileRenderer INSTANCE = (filePath, model, ctx) -> {
    try {
      Handlebars handlebars = new Handlebars();
      Template template = handlebars.compile(filePath);
      return template.apply(model);
    } catch (Exception e) { // IOException o HandlebarsException

      throw new ErrorRenderizadoException("Error al renderizar template: " + filePath, e);
    }
  };

}