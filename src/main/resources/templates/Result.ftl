<#-- @ftlvariable name="" type="de.ialistannen.javadocapi.server.views.ResultView" -->
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cool JavaDoc result</title>
  <style>
    .container {
      display: flex;
      align-items: center;
      justify-content: start;
      flex-wrap: wrap;
    }

    code {
      white-space: pre-wrap !important;
      word-wrap: break-word !important;
      font-family: monospace !important;
      border: 1px solid gray;
      margin: 8px;
      padding: 4px;
    }

    .split {
      width: 100%;
      display: flex;
      flex-wrap: nowrap;
    }

    .split > * {
      max-width: 50%;
      width: 50%;
    }

    .row {
      flex-basis: 100%;
      flex-shrink: 0;
    }

    h1 {
      text-align: center;
    }

    .container.centering {
      align-items: center;
      justify-content: center;
    }

    input[type="text"] {
      font-size: 24px;
      font-family: monospace;
      width: 40ch;
    }
  </style>
  <link href="https://unpkg.com/prismjs@1/themes/prism.css" rel="stylesheet"/>
  <script src="https://unpkg.com/prismjs@1/components/prism-core.min.js"></script>
  <script src="https://unpkg.com/prismjs@1/plugins/autoloader/prism-autoloader.min.js"></script>
</head>
<body>

<div class="container centering">
  <h1 class="row">Perform Query</h1>
  <form method="GET" action="/search">
    <label>
      <input type="text" name="query" placeholder="Enter text...">
    </label>
    <button type="submit">Search</button>
  </form>
</div>

<#if (results?size > 0)>
  <h1>Query results</h1>
  <div class="container">
      <#list results as result>
        <div class="container">
          <h3 class="row">
            <code class="language-java">${result.declaration}</code>
              <#if (result.name.asString()?contains("#"))>
                in
                <a href="/search?query=${result.name.lexicalParent.get().asString()}">
                  <code class="language-java">${result.name.lexicalParent.get().asString()}</code>
                </a>
              </#if>
              ${result.exact?string("exact match", "fuzzy match")}
          </h3>
          <div class="split row">
            <code class="language-html">${result.html}</code>
            <code class="language-markdown">${result.markdown}</code>
          </div>
          <footer>
            Loaded from API: '${result.source}', qualified to ${result.name.asString()}
          </footer>
        </div>
      </#list>
  </div>
</#if>
</body>
</html>

