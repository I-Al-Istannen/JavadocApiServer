package de.ialistannen.javadocapi.server.endpoints;

import de.ialistannen.javadocapi.model.QualifiedName;

public class Result {

  private final boolean exact;
  private final QualifiedName name;
  private final String markdown;
  private final String html;
  private final String source;
  private final String declaration;

  public Result(boolean exact, QualifiedName name, String markdown, String html, String source,
      String declaration) {
    this.exact = exact;
    this.name = name;
    this.markdown = markdown;
    this.html = html;
    this.source = source;
    this.declaration = declaration;
  }

  public boolean isExact() {
    return exact;
  }

  public QualifiedName getName() {
    return name;
  }

  public String getMarkdown() {
    return markdown;
  }

  public String getHtml() {
    return html;
  }

  public String getSource() {
    return source;
  }

  public String getDeclaration() {
    return declaration;
  }
}
