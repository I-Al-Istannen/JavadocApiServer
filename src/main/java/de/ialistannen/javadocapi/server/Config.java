package de.ialistannen.javadocapi.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Config extends Configuration {

  private final List<String> paths;

  @JsonCreator
  public Config(@JsonProperty("database_locations") List<String> paths) {
    this.paths = paths;
  }

  public List<Path> getDatabasePaths() {
    return paths.stream().map(Path::of).collect(Collectors.toList());
  }
}
