package ar.edu.utn.frba.dds.model.fuentes.metamapa;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PathAdapter extends TypeAdapter<Path> {
  @Override
  public void write(JsonWriter out, Path value) throws IOException {
    out.value(value == null ? null : value.toString()); // como string tipo "D:/archivos/foo.txt"
  }

  @Override
  public Path read(JsonReader in) throws IOException {
    return Path.of(in.nextString());
  }
}
