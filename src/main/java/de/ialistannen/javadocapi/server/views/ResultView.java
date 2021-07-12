package de.ialistannen.javadocapi.server.views;

import de.ialistannen.javadocapi.server.endpoints.Result;
import io.dropwizard.views.View;
import java.util.List;

public class ResultView extends View {

  private final List<Result> results;

  public ResultView(List<Result> results) {
    super("/templates/Result.ftl");
    this.results = results;
  }

  public List<Result> getResults() {
    return results;
  }
}
