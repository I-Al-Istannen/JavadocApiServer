package de.ialistannen.javadocapi.server;

import de.ialistannen.javadocapi.querying.FuzzyElementQuery;
import de.ialistannen.javadocapi.rendering.HtmlCommentRender;
import de.ialistannen.javadocapi.rendering.MarkdownCommentRenderer;
import de.ialistannen.javadocapi.server.endpoints.DocEndpoint;
import de.ialistannen.javadocapi.storage.AggregatedElementLoader;
import de.ialistannen.javadocapi.storage.ConfiguredGson;
import de.ialistannen.javadocapi.storage.ElementLoader;
import de.ialistannen.javadocapi.storage.SqliteStorage;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application<Config> {

  @Override
  public String getName() {
    return "JavadocAPI Server";
  }

  @Override
  public void run(Config configuration, Environment environment) {
    List<ElementLoader> loaders = configuration.getDatabasePaths().stream()
        .map(it -> new SqliteStorage(ConfiguredGson.create(), it))
        .collect(Collectors.toList());

    environment.jersey().register(new DocEndpoint(
        new FuzzyElementQuery(),
        new AggregatedElementLoader(loaders),
        new MarkdownCommentRenderer(),
        new HtmlCommentRender()
    ));
  }

  public void initialize(Bootstrap<Config> bootstrap) {
    bootstrap.addBundle(new ViewBundle<>());
  }

  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
