package ar.edu.utn.frba.dds.model.converters;

import java.nio.file.Path;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PathToStringConverter implements AttributeConverter<Path, String> {

  @Override
  public String convertToDatabaseColumn(Path attribute) {
    return (attribute != null) ? attribute.toString() : null;
  }

  @Override
  public Path convertToEntityAttribute(String dbData) {
    return (dbData != null) ? Path.of(dbData) : null;
  }
}