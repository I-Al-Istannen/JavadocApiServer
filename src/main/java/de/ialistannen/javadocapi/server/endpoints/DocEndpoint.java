package de.ialistannen.javadocapi.server.endpoints;

import de.ialistannen.javadocapi.model.JavadocElement;
import de.ialistannen.javadocapi.model.JavadocElement.DeclarationStyle;
import de.ialistannen.javadocapi.model.comment.JavadocComment;
import de.ialistannen.javadocapi.model.comment.JavadocCommentTag;
import de.ialistannen.javadocapi.querying.FuzzyElementQuery;
import de.ialistannen.javadocapi.rendering.HtmlCommentRender;
import de.ialistannen.javadocapi.rendering.MarkdownCommentRenderer;
import de.ialistannen.javadocapi.server.views.ResultView;
import de.ialistannen.javadocapi.storage.ConfiguredGson;
import de.ialistannen.javadocapi.storage.ElementLoader;
import io.dropwizard.views.View;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;

@Path("/")
public class DocEndpoint {

  private final FuzzyElementQuery finder;
  private final ElementLoader loader;
  private final MarkdownCommentRenderer markdownCommentRenderer;
  private final HtmlCommentRender htmlCommentRender;

  public DocEndpoint(FuzzyElementQuery finder, ElementLoader loader,
      MarkdownCommentRenderer markdownCommentRenderer,
      HtmlCommentRender htmlCommentRender) {
    this.finder = finder;
    this.loader = loader;
    this.markdownCommentRenderer = markdownCommentRenderer;
    this.htmlCommentRender = htmlCommentRender;
  }

  @GET
  @Path("/search")
  @Produces(MediaType.TEXT_HTML)
  public View query(@QueryParam("query") String query) {
    if (query == null || query.isBlank()) {
      return new ResultView(List.of());
    }

    return new ResultView(getResultsForQuery(query));
  }

  @GET
  @Path("/search")
  @Produces(MediaType.APPLICATION_JSON)
  public String queryAsJson(@QueryParam("query") String query) {
    if (query == null || query.isBlank()) {
      return "[]";
    }

    return ConfiguredGson.create().toJson(getResultsForQuery(query));
  }

  private List<Result> getResultsForQuery(@QueryParam("query") String query) {
    return finder.query(loader, query)
        .stream()
        .flatMap(it -> loader.findByQualifiedName(it.getQualifiedName())
            .stream()
            .map(result -> {
              JavadocElement element = result.getResult();
              String html = renderCommentToHtml(element);
              String markdown = renderCommentToMarkdown(element);

              return new Result(
                  it.isExact(),
                  element.getQualifiedName(),
                  markdown,
                  html,
                  result.getLoader().toString(),
                  element.getDeclaration(DeclarationStyle.SHORT)
              );
            })
        )
        .collect(Collectors.toList());
  }

  private String renderCommentToHtml(JavadocElement element) {
    if (element.getComment().isEmpty()) {
      return "none!";
    }
    JavadocComment comment = element.getComment().get();

    String result = "";

    result += htmlCommentRender.render(comment.getShortDescription());
    result += "<br>";
    result += htmlCommentRender.render(comment.getLongDescription());
    result += "<br>";
    result += "<ul>";
    for (JavadocCommentTag tag : comment.getTags()) {
      result += "<em>" + tag.getTagName() + tag.getArgument().orElse("") + "</em>";
      result += htmlCommentRender.render(tag.getContent());
    }
    result += "</ul>";

    Document document = Jsoup.parse(result);
    document.outputSettings()
        .indentAmount(2)
        .prettyPrint(true)
        .syntax(Syntax.html);

    return document.getElementsByTag("body").get(0).toString();
  }


  private String renderCommentToMarkdown(JavadocElement element) {
    if (element.getComment().isEmpty()) {
      return "none!";
    }
    JavadocComment comment = element.getComment().get();

    String result = "";

    result += markdownCommentRenderer.render(comment.getShortDescription());
    result += "  \n";
    result += markdownCommentRenderer.render(comment.getLongDescription());
    result += "  \n";
    for (JavadocCommentTag tag : comment.getTags()) {
      result += "- **" + tag.getTagName() + tag.getArgument().orElse("") + "**";
      result += "  \n";
      result += htmlCommentRender.render(tag.getContent()).indent(2);
      result += "\n";
    }

    return result;
  }
}
